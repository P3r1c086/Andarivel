package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class EstadoTrabajadoresPresenter extends BasePresenter<EstadoTrabajadoresView> {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void fichajesCurrentUsers() {
        database.getFichajes(task -> {
            if (task.isSuccessful()) {
                ArrayList<Fichaje> listaFichajes = new ArrayList<>();
                //En este mapa obtenemos un mapa con todos los fichajes.
                Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                if (mapRaiz != null && !mapRaiz.isEmpty()) {
                    for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                        //En este mapa obtenemos los datos de cada fichaje.
                        Map<String, String> mapFichaje = (Map<String, String>) entry.getValue();
                        Fichaje fichaje = new Fichaje();
                        fichaje.setIDUsuario(mapFichaje.get("usuario"));
                        fichaje.setFecha(mapFichaje.get("fecha"));
                        fichaje.setHoraEntrada(mapFichaje.get("horaEntrada"));
                        fichaje.setHoraSalida(mapFichaje.get("horaSalida"));
                        listaFichajes.add(fichaje);
                    }
                    database.getInfoUsers(task1 -> {
                        if (task1.isSuccessful()) {
                            ArrayList<Fichaje> listaFichajesCompleta = new ArrayList<>();
                            //Obtenemos un mapa con todos los usuarios.
                            Map<String, Object> mapRaiz1 = (Map<String, Object>) task1.getResult().getValue();
                            //Recorremos la informacion de todos los usuarios.
                            for (Map.Entry<String, Object> entry : mapRaiz1.entrySet()) {
                                //Obtenemos un mapa con los datos de los fichajes.
                                Map<String, String> mapInfoTodosUsuarios = (Map<String, String>) entry.getValue();
                                //Nos devuelve el fichaje que coincide con el id del usuario.
                                Fichaje f = findFichaje(listaFichajes, entry.getKey());
                                //Mientras existan el fichajes.
                                while (f != null) {
                                    //Quita el fichaje encontrado de la lista de fichajes.
                                    listaFichajes.remove(f);
                                    //Le concatenamos el valor de nombre y apelldos del usuario y se lo asignamos al fichaje.
                                    f.setNombreUsuario(mapInfoTodosUsuarios.get("nombre") + " " + mapInfoTodosUsuarios.get("apellidos"));
                                    //Añadimos el fichaje con todos los datos necesarios a la listaFichajesCompleta
                                    listaFichajesCompleta.add(f);
                                    //Volvemos a buscar por si hay mas fichajes de ese usuario.
                                    f = findFichaje(listaFichajes, entry.getKey());
                                }
                            }
                            SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                            String date1 = dateformat.format(Calendar.getInstance().getTime().getTime()).trim();
                            ArrayList<Fichaje> listaFichajesDefinitiva = new ArrayList<>();
                            for (int i = 0; i < listaFichajesCompleta.size(); i++) {
                                //si la fecha que hay en la bd y la fecha actual no son iguales, borra el fichaje
                                if (listaFichajesCompleta.get(i).getFecha().equals(date1)) {
                                    listaFichajesDefinitiva.add(listaFichajesCompleta.get(i));
                                }
                            }
                            view.agnadirListaUsuarios(listaFichajesDefinitiva);
                        }
                    });
                } else {
                    view.mostrarListaVacia();
                }
            } else {
                view.mostrarFalloFirebase();
            }
        });
    }

    /**
     * Encuentra el fichaje que coincida con el id del usuaario
     *
     * @param list   de objetos Fichaje
     * @param idUser identificador del usuario
     * @return el objeto Fichaje
     */
    private Fichaje findFichaje(ArrayList<Fichaje> list, String idUser) {
        for (Fichaje f : list) {
            if (f.getIDUsuario().equals(idUser)) {
                return f;
            }
        }
        return null;
    }
}
