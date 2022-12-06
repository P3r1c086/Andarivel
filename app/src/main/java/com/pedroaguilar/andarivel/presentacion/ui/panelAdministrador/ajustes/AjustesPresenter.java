package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ajustes;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ajustes
 * Create by Pedro Aguilar Fernández on 06/12/2022 at 10:01
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class AjustesPresenter extends BasePresenter<AjustesView> {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    public void borrarUsuario() {
        database.borrarUsuario(firebaseAuth.getUid(), task -> {
            //Por ultimo borramos el usuario del autenticador
            firebaseAuth.getCurrentUser().delete();
            view.navegarAlLogin();
        });
    }

    public void borrarFotoPerfil() {
        //Borramos la foto de perfil del Storage de Firebaswe
        storage.borrarFotoPerfil(firebaseAuth.getUid());
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        view.navegarAlLogin();
    }
}
