package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.UploadTask;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

public class PerfilPresenter extends BasePresenter<PerfilView>{

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    public void getInfoUser(){
        //obtengo el hijo de usuarios con este id
        //si quiero solo una foto o momento, es decir, que no me llegue tod el rato llamo a get()
        database.getInfoUser(firebaseAuth.getUid(), (OnCompleteListener<DataSnapshot>) task -> {
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
    public void borrarUsuario(){
        database.borrarUsuario(firebaseAuth.getUid(), (OnCompleteListener<Void>) task -> {
            //Por ultimo borramos el usuario del autenticador
            firebaseAuth.getCurrentUser().delete();
            view.navegarAlLogin();
        });
    }
    public void guardaImagenPerfil(byte[] data){
        storage.guardarImagenDePerfil(FirebaseAuth.getInstance().getUid(), data,
                e -> view.showErrorSubirImagen(),
                (OnCompleteListener<UploadTask.TaskSnapshot>) task -> {
            view.setImagenUsuario(storage.getUserPerfilUrl(firebaseAuth.getUid()));
        });
    }


}