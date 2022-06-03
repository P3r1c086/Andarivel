package com.pedroaguilar.andarivel.servicios;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.Map;

public class ServicioFirebaseDatabase {

    private final FirebaseDatabase firebaseDataBase =  FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference(Constantes.NODO_USUARIOS);
    private final DatabaseReference databaseReferenceFichaje = FirebaseDatabase.getInstance().getReference(Constantes.NODO_FICHAJE);

    private final DatabaseReference databaseReferenceAusencia = FirebaseDatabase.getInstance().getReference(Constantes.NODO_AUSENCIAS);

    //Zona Usuario
    public void crearUsuario(String firebaseAuthUsuarioId, Usuario user){
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId).setValue(user);
    }

    public void actualizarDatosUsuario(String firebaseAuthUsuarioId, Map<String, Object> childUpdates, OnCompleteListener<Void> listener){
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId).updateChildren(childUpdates).addOnCompleteListener(listener);
    }

    public void getInfoUsers(OnCompleteListener<DataSnapshot> listener){
        databaseReferenceUsuarios
                .get()
                .addOnCompleteListener(listener);
    }

    public void getInfoUser(String firebaseAuthUsuarioId, OnCompleteListener<DataSnapshot> listener){
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId)
                .get()
                .addOnCompleteListener(listener);
    }

    public void borrarUsuario(String firebaseAuthUsuarioId, OnCompleteListener<Void> listener){
        getInfoUser(firebaseAuthUsuarioId, new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> fichajes = (Map<String, Object>)((Map<String, Object>) task.getResult().getValue()).get("Fichajes");
                    if (fichajes != null) {
                        for (String key :
                                fichajes.keySet()) {
                            fichajes.put(key, null);
                        }
                        databaseReferenceFichaje.updateChildren(fichajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReferenceUsuarios
                                        .child(firebaseAuthUsuarioId).removeValue().addOnCompleteListener(listener);
                            }
                        });
                    } else {
                        databaseReferenceUsuarios
                                .child(firebaseAuthUsuarioId).removeValue().addOnCompleteListener(listener);
                    }
                }
            }
        });


    }

    //Fin Zona Usuario

    //Zona Fichaje

    public void cuentaFichaje(OnCompleteListener<DataSnapshot> listener) {
        firebaseDataBase.getReference(Constantes.NODO_FICHAJE).get().addOnCompleteListener(listener);
    }

    public void actualizarFichaje(Map<String, Object> childUpdates){
        databaseReferenceFichaje
                .updateChildren(childUpdates);
    }

    public void getFichajes(OnCompleteListener<DataSnapshot> listener){
        databaseReferenceFichaje
                .get()
                .addOnCompleteListener(listener);
    }

    //Fin Zona Fichaje

    //Zona Ausencia

    public void cuentaAusencia(OnCompleteListener<DataSnapshot> listener) {
        firebaseDataBase.getReference(Constantes.NODO_AUSENCIAS).get().addOnCompleteListener(listener);
    }
    public void actualizarAusencia(Map<String, Object> childUpdates, OnCompleteListener<Void> listener){
        databaseReferenceAusencia
                .updateChildren(childUpdates)
        .addOnCompleteListener(listener);
    }
    public void getAusencias(OnCompleteListener<DataSnapshot> listener){
        databaseReferenceAusencia
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAusencias(ValueEventListener listener){
        databaseReferenceAusencia
                .addValueEventListener(listener);
    }

    //Fin Zona Ausencia

}
