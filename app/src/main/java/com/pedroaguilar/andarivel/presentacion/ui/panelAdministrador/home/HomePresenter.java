package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.home;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.utilidades.SortDescendingComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class HomePresenter extends BasePresenter<HomeView>{

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void getFichajes(){
        //Obtenemos todos los fichajes y colocamos la app en el estadoFinJornada o estadoInicioJornada dependiendo de si encuentra algun fichaje
        // completado o sin completar.
        database.getFichajes(task -> {
            if (task.isSuccessful()){
                Boolean estadoFinJornada = false;
                //Creamos un mapa con todos los fichajes sin ordenar.
                Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                //Creamos otro mapa con todos los fichajes anteriores ordenados descendentemente, es decir, el primmero seria
                // el ultimo  que se ha introducido.
                TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                //Añadimos entries del mapa devuelto por firebase
                if (fichajes!= null ) sorted.putAll(fichajes);
                //Recorremos el mapa ordenado buscando el usuario que coincida con el de la sesión y que tenga
                // un nodo con horaSalida a null (que no exista el nodo)
                for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                    if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))
                            && ((Map<String, Object>) entry.getValue()).get("horaSalida") == null){
                        estadoFinJornada = true;
                        break;
                    }
                }
                //En función de si lo hemos encontrado o no, setteamos en la pantalla un estado u otro
                if (estadoFinJornada){
                    view.setEstadoFinalJornada();
                } else {
                    view.setEstadoInicioJornada();
                }

            }
        });
    }

    public void almacenarFechaYhoraInicial(String fecha, String horaEntrada) {
        database.cuentaFichaje(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getValue() == null) {
                    crearNodoFichaje("1", fecha, horaEntrada);
                } else {
                    crearNodoFichaje((((Map<String, Object>) task.getResult().getValue()).entrySet().size() + 1) + "", fecha, horaEntrada);
                }
            } else {

            }
        });
    }

    private void crearNodoFichaje(String nNodo, String fecha, String horaEntrada){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Fichaje"+nNodo+"/fecha", fecha);
        childUpdates.put("/Fichaje"+nNodo+"/horaEntrada", horaEntrada);
        childUpdates.put("/Fichaje"+nNodo+"/usuario", mAuth.getUid());
        database.actualizarFichaje(childUpdates);
        //Hacemos un clear para reutilizar el mapa.
        childUpdates.clear();
        childUpdates.put("/Fichajes/Fichaje"+nNodo, true);
        database.actualizarDatosUsuario(mAuth.getUid(), childUpdates, task -> { });
    }

    public void almacenarFechaYhoraFinal(String horaSalida, String tiempoTrabajado) {
        database.getFichajes(task -> {
            if (task.isSuccessful()) {
                String nodoFichaje = "";
                //Mapa completamente desordenado, devuelto por firebase
                Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                //Construimos TreeMap con nuestro comparador de ordenacion descendente
                TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                //Añadimos entries del mapa devuelto por firebase
                sorted.putAll(fichajes);
                //Recorremos el mapa ordenado buscando valor que coincida con el usuario.
                for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                    //key                          //value
                    if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))){
                        nodoFichaje = entry.getKey(); //me devuelve fichaje n
                        break;
                    }
                }
                //Actualizamos campo horaSalida del nodo encontrado.
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + nodoFichaje + "/horaSalida", horaSalida);
                childUpdates.put("/" + nodoFichaje + "/tiempoTrabajado", tiempoTrabajado);
                database.actualizarFichaje(childUpdates);
            }
        });

    }
}
