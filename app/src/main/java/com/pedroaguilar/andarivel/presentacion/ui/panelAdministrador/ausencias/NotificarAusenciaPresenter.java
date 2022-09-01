package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;
import com.pedroaguilar.andarivel.utilidades.SortDescendingComparator;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class NotificarAusenciaPresenter extends BasePresenter<NotificarAusenciaView> {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    Ausencia ausenciaMostrada = null;
    File localDoc = null;

    public void obtenerAusencias() {
        //Accedemos a todas las ausencias.
        database.getAusencias(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Map<String, Object> mapRaiz = (Map<String, Object>) snapshot.getValue();
                    if (mapRaiz != null && !mapRaiz.isEmpty()) {
                        Boolean find = false;
                        //Creamos otro mapa con todos los fichajes anteriores ordenados descendentemente, es decir, el primmero seria
                        // el ultimo  que se ha introducido.
                        TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                        //Añadimos entries del mapa devuelto por firebase
                        sorted.putAll(mapRaiz);
                        for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                            Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                            //Si el valor del dato usuario es igual al id del usuario al que notificar la ausencia,
                            //obtenemos los datos de esa ausencia y los mostramos.
                            if (mapAusencia.get("usuario").equals(FirebaseAuth.getInstance().getUid())) {
                                Ausencia ausencia = new Ausencia();
                                ausencia.setIdAusencia(mapAusencia.get("usuario"));
                                ausencia.setFechaInicioAusencia(mapAusencia.get("fechaInicio"));
                                ausencia.setFechaFinAusencia(mapAusencia.get("fechaFin"));
                                ausencia.setMotivoAusencia(mapAusencia.get("motivoAusencia"));
                                ausencia.setDescripcionAusencia(mapAusencia.get("descripcion"));
                                ausencia.setEstado(mapAusencia.get("estado"));
                                if (mapAusencia.get("adjunto") != null) {
                                    ausencia.setAdjunto(mapAusencia.get("adjunto"));
                                }
                                ausenciaMostrada = ausencia;
                                view.showAusencia(ausencia);
                                find = true;
                                break;
                            }
                        }
                        if (!find) {
                            view.showNoHayAusencias();
                        }
                    } else {
                        view.showNoHayAusencias();
                    }
                } else {
                    view.showNoHayAusencias();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickBotonAdjunto(File fileTemp, OnSuccessListener<FileDownloadTask.TaskSnapshot> onSuccessListener){
        localDoc = fileTemp;
        storage.descargarYVerDocumentoAdjunto(fileTemp, firebaseAuth.getUid(), ausenciaMostrada.getAdjunto(), onSuccessListener);
    }


}
