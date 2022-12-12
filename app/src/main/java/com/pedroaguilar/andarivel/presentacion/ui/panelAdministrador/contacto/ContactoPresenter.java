package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.contacto;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.contacto
 * Create by Pedro Aguilar Fernández on 10/12/2022 at 18:49
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class ContactoPresenter extends BasePresenter<ContactoView> {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void leerDatoUsuario() {
        database.getInfoUser(firebaseAuth.getCurrentUser().getUid(), task -> {
            String nombre = null;
            String apellidos = null;
            if (task.isSuccessful()) {
                //obtengo los datos de firebase
                nombre = "" + task.getResult().child("nombre").getValue();
                apellidos = "" + task.getResult().child("apellidos").getValue();
            }
            view.setDatosMailUsuario(nombre, apellidos);
        });
    }
}
