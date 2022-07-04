package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.util.Map;

public class PanelAdministradorPresenter extends BasePresenter<PanelAdministradorView> {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    public void logicaPanelAdministrador() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //Obtenemos la informacion del usuario que ha iniciado sesion.
            database.getInfoUser(firebaseAuth.getUid(), task -> {
                if (task.isSuccessful()) {
                    //Esta variable almacenara true o false dependiendo el valor que encuentre en el campo esAdministrador
                    Boolean esAdmin = (Boolean) ((Map<String, Object>) task.getResult().getValue()).get("esAdiminstrador");
                    //Si es administrador se cargara el panel del administrador y si no el del empleado.
                    view.inflarVistaSegunPerfil(esAdmin);
                    loadHeaderInfoUser();
                }
            });
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        view.navegarAlLogin();
    }

    public void loadHeaderInfoUser() {
        view.setImageHeaderDrawer(storage.getUserPerfilUrl(firebaseAuth.getUid()));
        database.getInfoUser(firebaseAuth.getUid(), task -> {
            if (task.isSuccessful()) {
                String nombre = "" + task.getResult().child("nombre").getValue();
                String apellidos = "" + task.getResult().child("apellidos").getValue();
                String email = "" + task.getResult().child("email").getValue();
                view.setTextHeaderDrawer(nombre, apellidos, email);
            }
        });
    }
}
