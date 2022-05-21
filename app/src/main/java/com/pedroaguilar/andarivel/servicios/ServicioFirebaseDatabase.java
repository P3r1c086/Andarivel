package com.pedroaguilar.andarivel.servicios;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.pedroaguilar.andarivel.databinding.FragmentNuevoUsuarioBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.ui.login.NuevoUsuarioFragment;

import java.util.ArrayList;
import java.util.Map;

public class ServicioFirebaseDatabase {

    private final FirebaseDatabase firebaseDataBase =  FirebaseDatabase.getInstance();
    private DatabaseReference databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference(Constantes.TABLA_USUARIOS);
    private DatabaseReference databaseReferenceAusencia = FirebaseDatabase.getInstance().getReference(Constantes.TABLA_AUSENCIAS);
    private DatabaseReference databaseReferenceFichaje = FirebaseDatabase.getInstance().getReference(Constantes.TABLA_HORARIOS);
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private NuevoUsuarioFragment nu = new NuevoUsuarioFragment();
    private ArrayList<Usuario> arrayListUsuarios = new ArrayList<Usuario>();
    //private Adaptador adaptador = new Adaptador();
    private FragmentNuevoUsuarioBinding nuevoUsuarioBinding;

    public void leerUsuario(OnCompleteListener<DataSnapshot> listener){
        databaseReferenceUsuarios.child(user.getUid()).get().addOnCompleteListener(listener);
    }
//    public void leerTodosUsuariosDatabase(){
//        databaseReferenceUsuarios.child(Constantes.TABLA_USUARIOS).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for (DataSnapshot ds: snapshot.getChildren()) {
//                        String nombre = ds.child("nombre").getValue().toString();
//                            arrayListUsuarios.add(new Usuario(nombre));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void crearUsuario(String firebaseAuthUsuarioId, Usuario user){
        databaseReferenceUsuarios.child(Constantes.TABLA_USUARIOS).child(firebaseAuthUsuarioId).setValue(user);
    }

    public void ficharEntrada(String firebaseAuthUsuarioId, Usuario user){
        //Crear nodo FichajeN, apuntar FichajeN en Fichajes del usuario y apuntar en FichajesDiarios
        databaseReferenceFichaje.child(Constantes.TABLA_HORARIOS).child(firebaseAuthUsuarioId).setValue(user);
    }

    public void ficharSalida(String firebaseAuthUsuarioId, Map<String, Object> childUpdates){
        databaseReferenceFichaje.child(Constantes.TABLA_HORARIOS).child(firebaseAuthUsuarioId).updateChildren(childUpdates);
    }

    public void crearAusencia(String firebaseAuthUsuarioId, Usuario user){
        databaseReferenceAusencia.child(Constantes.TABLA_AUSENCIAS).child(firebaseAuthUsuarioId).setValue(user);
    }

    public void actualizarDatosUsuario(String firebaseAuthUsuarioId,Map<String, Object> childUpdates){
        databaseReferenceUsuarios.child(Constantes.TABLA_USUARIOS).child(firebaseAuthUsuarioId).updateChildren(childUpdates);
    }


    public void incrementCounter() {
        //Método para incrementar en 1 el id de la base de datos
        firebaseDataBase.getReference(Constantes.TABLA_USUARIOS).runTransaction(new Transaction.Handler() {
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
            @Override
            public void onComplete(@Nullable DatabaseError firebaseError, boolean committed, @Nullable DataSnapshot currentData) {
                if (firebaseError != null) {
                    Log.d(ServicioFirebaseDatabase.class.getSimpleName(), "Firebase counter increment failed.");
                } else {
                    Log.d(ServicioFirebaseDatabase.class.getSimpleName(), "Firebase counter increment succeeded.");
                }
            }
        });
    }

    /**
     * Método para autenticar el email y la contraseña
     */
    public void autenticar(){

    }
}
