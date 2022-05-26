package com.pedroaguilar.andarivel.servicios;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public void actualizarDatosUsuario(String firebaseAuthUsuarioId, Map<String, Object> childUpdates){
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId).updateChildren(childUpdates);
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


    public void crearAusencia(String firebaseAuthUsuarioId, Usuario user){
        databaseReferenceAusencia.child(Constantes.NODO_AUSENCIAS).child(firebaseAuthUsuarioId).setValue(user);
    }

    /*
    new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }

                return Transaction.success(currentData);
            }

            /**
             * This method will be called once with the results of the transaction.
             *
             * @param firebaseError       null if no errors occurred, otherwise it contains a description of the error
             * @param committed   True if the transaction successfully completed, false if it was aborted or
             *                    an error occurred
             * @param currentData The current data at the location or null if an error occurred
             */
   /* @Override
    public void onComplete(@Nullable DatabaseError firebaseError, boolean committed, @Nullable DataSnapshot currentData) {
        if (firebaseError != null) {
            Log.d(ServicioFirebaseDatabase.class.getSimpleName(), "Firebase counter increment failed.");
        } else {
            Log.d(ServicioFirebaseDatabase.class.getSimpleName(), "Firebase counter increment succeeded.");
        }
    }
}*/

}
