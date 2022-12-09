package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConcederAusenciaPresenter extends BasePresenter<ConcederAusenciaView> {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    File localDoc = null;
    ArrayList<Ausencia> listaAusencias = new ArrayList<>();

    public void leerTodosUsuariosDatabase() {
        //Accedemos a todas las ausencias
        database.getAusencias(task -> {
            if (task.isSuccessful()) {
                listaAusencias = new ArrayList<>();
                //Este mapa nos devuelve el nodo ausencia y todos los ausencia n que contenga.
                Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                if (mapRaiz != null && !mapRaiz.isEmpty()) {
                    //Con un foreach obtenemos los datos de todas las ausencias y las almacenamos en un array de Ausencia.
                    for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                        Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                        Ausencia ausencia = new Ausencia();
                        ausencia.setIdAusencia(entry.getKey());
                        ausencia.setNombreUsuario(mapAusencia.get("usuario"));
                        ausencia.setIdUsuario(mapAusencia.get("usuario"));
                        ausencia.setFechaInicioAusencia(mapAusencia.get("fechaInicio"));
                        ausencia.setFechaFinAusencia(mapAusencia.get("fechaFin"));
                        ausencia.setMotivoAusencia(mapAusencia.get("motivoAusencia"));
                        ausencia.setDescripcionAusencia(mapAusencia.get("descripcion"));
                        ausencia.setEstado(mapAusencia.get("estado"));
                        if (mapAusencia.get("adjunto") != null) {
                            ausencia.setAdjunto(mapAusencia.get("adjunto"));
                        }
                        listaAusencias.add(ausencia);
                    }
                    //Accedemos a la informacion de todos los usuarios.
                    database.getInfoUsers(task1 -> {
                        if (task1.isSuccessful()) {
                            ArrayList<Ausencia> listaAusenciasCompleta = new ArrayList<Ausencia>();
                            Map<String, Object> mapRaiz1 = (Map<String, Object>) task1.getResult().getValue();
                            for (Map.Entry<String, Object> entry : mapRaiz1.entrySet()) {
                                Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                                Ausencia a = findAusencia(listaAusencias, entry.getKey());
                                while (a != null) {
                                    listaAusencias.remove(a);
                                    a.setNombreUsuario(mapAusencia.get("nombre") + " " + mapAusencia.get("apellidos"));
                                    listaAusenciasCompleta.add(a);
                                    a = findAusencia(listaAusencias, entry.getKey());
                                }

                            }
                            view.agregarAusenciasAadaptador(listaAusenciasCompleta);
                        }
                    });
                } else {
                    view.mostrarInfoNoPeticionesAusencia();
                }

            } else {
                view.mostrarFalloLecturaBaseDeDatos();
            }
        });
    }

    /**
     * Metodo para encontrar la ausencia que pertenece a un usuario
     */
    private Ausencia findAusencia(ArrayList<Ausencia> list, String idUser) {
        for (Ausencia a : list) {
            if (a.getNombreUsuario().equals(idUser)) {
                return a;
            }
        }
        return null;
    }

    public void onClickBotonAdjunto(File fileTemp, Ausencia ausencia, OnCompleteListener<FileDownloadTask.TaskSnapshot> onCompleteListener) {
        localDoc = fileTemp;
        storage.descargarYVerDocumentoAdjunto(fileTemp, ausencia.getIdUsuario(), ausencia.getAdjunto(), onCompleteListener);
    }
}
