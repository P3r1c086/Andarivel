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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase externaliza las conexiones a la base de datos y al autenticador de Firebase
 */

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

    public void getInfoUser(String firebaseAuthUsuarioId, OnCompleteListener<DataSnapshot> listener) {
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getFichajesUser(String firebaseAuthUsuarioId, OnCompleteListener<DataSnapshot> listener) {
        databaseReferenceUsuarios
                .child(firebaseAuthUsuarioId)
                .child("Fichajes")
                .get()
                .addOnCompleteListener(listener);
    }

    public void borrarUsuario(String firebaseAuthUsuarioId, OnCompleteListener<Void> listener) {
        getInfoUser(firebaseAuthUsuarioId, task -> {
            if (task.isSuccessful()) {
                //Accedemos al mapa fichajes del nodo del usuario
                Map<String, Object> fichajes = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get("Fichajes");
                if (fichajes != null) {
                    //Nos guardamos las referencias de los fichajes del usuario
                    for (String key : fichajes.keySet()) {
                        fichajes.put(key, null);
                    }
                    //Primero borramos el nodo fichajes del usuario asi como sus hijos.
                    databaseReferenceFichaje.updateChildren(fichajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Borra tod el nodo del usuario.
                            databaseReferenceUsuarios
                                    .child(firebaseAuthUsuarioId).removeValue().addOnCompleteListener(listener);
                        }
                    });
                } else {
                    //Si no hay fichajes borra tod el nodo del usuario.
                    databaseReferenceUsuarios
                            .child(firebaseAuthUsuarioId).removeValue().addOnCompleteListener(listener);
                }
            }
        });
    }

    //todo
    public void borrarAusencia(OnCompleteListener<Void> listener) {
        getInfoUsers(task -> {
            if (task.isSuccessful()) {
                HashMap<String, Object> users = (HashMap<String, Object>) task.getResult().getValue();
                if (users != null) {
                    for (String key : users.keySet()) {
                        users.put(key, null);
                    }
                }
                Set<String> keyUsers = users.keySet();
                ArrayList<String> listOfKeys = new ArrayList<String>(keyUsers);
                    for (int i = 0; i < users.size(); i++) {
                        int finalI = i;
                        databaseReferenceUsuarios.child(listOfKeys.get(i)).child("Ausencias").get().addOnCompleteListener(task1 -> {
                           if (task1.isSuccessful()){
                               Map<String, Object> ausenciasDentroUsers = (Map<String, Object>) task1.getResult().getValue();
                               //todo buscar la ausencia de las variables globales que coincida y eliminarla
                               getAusencias(task2 -> {
                                   if (task2.isSuccessful()){
                                       HashMap<String, Object> ausenciasGlobales = (HashMap<String, Object>) task2.getResult().getValue();
                                       if (ausenciasGlobales != null){
                                           for (String key : ausenciasGlobales.keySet()) {
                                               ausenciasGlobales.put(key, null);
                                           }
                                       }
                                       Set<String> keyAus = ausenciasGlobales.keySet();
                                       ArrayList<String> listOfKeysAus = new ArrayList<String>(keyAus);
                                       if (listOfKeysAus.contains(ausenciasDentroUsers)){
                                           databaseReferenceUsuarios.child(listOfKeys.get(finalI)).child("Ausencias").removeValue();
                                           databaseReferenceAusencia.child("Ausencias").child(listOfKeysAus.get(finalI)).removeValue();
                                       }
                                   }
                               });
                           }
                        });
                    }
            }
        });
        // databaseReferenceAusencia.get().addOnCompleteListener(listener);/*child("Ausencia12").removeValue()*/
        //Log.i("////////////////////", pathAusencia);
        //pathAusencia.addOnCompleteListener(listener);
    }
    //Fin Zona Usuario

    //Zona Fichaje

    public void cuentaFichaje(OnCompleteListener<DataSnapshot> listener) {
        firebaseDataBase.getReference(Constantes.NODO_FICHAJE).get().addOnCompleteListener(listener);
    }

    public void actualizarFichaje(Map<String, Object> childUpdates) {
        databaseReferenceFichaje
                .updateChildren(childUpdates);
    }

    public void getFichajes(OnCompleteListener<DataSnapshot> listener) {
        databaseReferenceFichaje
                .get()
                .addOnCompleteListener(listener);
    }

    //todo
    public void borrarFichaje() {
    }

    //Fin Zona Fichaje

    //Zona Ausencia

    public void cuentaAusencia(OnCompleteListener<DataSnapshot> listener) {
        firebaseDataBase.getReference(Constantes.NODO_AUSENCIAS).get().addOnCompleteListener(listener);
    }

    public void actualizarAusencia(Map<String, Object> childUpdates, OnCompleteListener<Void> listener) {
        databaseReferenceAusencia
                .updateChildren(childUpdates)
                .addOnCompleteListener(listener);
    }

    public void getAusencias(OnCompleteListener<DataSnapshot> listener) {
        databaseReferenceAusencia
                .get()
                .addOnCompleteListener(listener);
    }

    /**
     * Obtiene los cambios producidos en una ausencia
     *
     * @param listener
     */
    public void getAusencias(ValueEventListener listener) {
        databaseReferenceAusencia
                .addValueEventListener(listener);
    }

    //Fin Zona Ausencia

}
