package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.Map;

public class AusenciasPresenter extends BasePresenter<AusenciasView> {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void infoUser() {
        database.getInfoUser(firebaseAuth.getUid(), task -> {
            if (task.isSuccessful()) {
                //Creamos una variable boolean para comprobar si el usuario es administrador o no
                Boolean esAdmin = (Boolean) ((Map<String, Object>) task.getResult().getValue()).get("esAdiminstrador");
                view.setInfoAdmin(esAdmin);
            }
        });
    }
}
