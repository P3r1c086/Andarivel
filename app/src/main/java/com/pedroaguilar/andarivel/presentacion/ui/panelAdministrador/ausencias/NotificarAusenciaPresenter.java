package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.Map;

public class NotificarAusenciaPresenter extends BasePresenter<NotificarAusenciaView> {

    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void obtenerAusencias() {
        //Accedemos a todas las ausencias.
        database.getAusencias(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Map<String, Object> mapRaiz = (Map<String, Object>) snapshot.getValue();
                    if (mapRaiz != null && !mapRaiz.isEmpty()) {
                        Boolean find = false;
                        for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
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

                                view.showAusencia(ausencia);
                                find = true;
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

}
