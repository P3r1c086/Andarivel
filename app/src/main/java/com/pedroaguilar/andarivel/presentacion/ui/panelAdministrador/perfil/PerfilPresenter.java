package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosPresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

public class PerfilPresenter extends CamaraYpermisosPresenter<PerfilView> {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void getInfoUser() {
        //obtengo el hijo de usuarios con este id
        //si quiero solo una foto o momento, es decir, que no me llegue tod el rato llamo a get()
        database.getInfoUser(firebaseAuth.getUid(), task -> {
            if (task.isSuccessful()) {
                //obtengo los datos de firebase
                String nombre = "" + task.getResult().child("nombre").getValue();
                String apellidos = "" + task.getResult().child("apellidos").getValue();
                String direccion = "" + task.getResult().child("direccion").getValue();
                String telefono = "" + task.getResult().child("telefono").getValue();
                String email = "" + task.getResult().child("email").getValue();

                view.setDatosUsuarioEnUi(nombre,
                        apellidos,
                        direccion,
                        telefono,
                        email,
                        storage.getUserPerfilUrl(firebaseAuth.getUid()));
            }
        });
    }

    public void borrarFotoPerfil(){
        //Borramos la foto de perfil del Storage de Firebaswe
        storage.borrarFotoPerfil(firebaseAuth.getUid());
    }

    public void borrarUsuario() {
        database.borrarUsuario(firebaseAuth.getUid(), task -> {
            //Por ultimo borramos el usuario del autenticador
            firebaseAuth.getCurrentUser().delete();
            view.navegarAlLogin();
        });
    }

    public void setImagenUser() {
        view.setImagenUsuario(storage.getUserPerfilUrl(firebaseAuth.getUid()));
    }

}
