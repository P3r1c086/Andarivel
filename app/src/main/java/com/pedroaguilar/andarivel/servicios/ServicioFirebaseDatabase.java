package com.pedroaguilar.andarivel.servicios;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase externaliza las conexiones a la base de datos y al autenticador de Firebase
 */

public class ServicioFirebaseDatabase {

    private final FirebaseDatabase firebaseDataBase =  FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference(Constantes.NODO_USUARIOS);
    private final DatabaseReference databaseReferenceFichaje = FirebaseDatabase.getInstance().getReference(Constantes.NODO_FICHAJE);
    private final DatabaseReference databaseReferenceAusencia = FirebaseDatabase.getInstance().getReference(Constantes.NODO_AUSENCIAS);
    private final DatabaseReference databaseReferenceAnuncio = FirebaseDatabase.getInstance().getReference(Constantes.NODO_ANUNCIO);


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

    public void borrarFichaje(Fichaje fichaje, OnCompleteListener<Void> listener) {
        getInfoUser(fichaje.getIDUsuario(), task -> {
            if (task.isSuccessful()) {
                //Accedemos al mapa fichajes del nodo del usuario
                Map<String, Object> fichajes = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get("Fichajes");
                Map<String, Object> fichajesUpdate = new HashMap<>();
                if (fichajes != null) {
                    //Nos guardamos las referencias de los ausencias del usuario
                    for (Map.Entry<String, Object> entry : fichajes.entrySet()) {
                        if (!fichaje.getIdFichaje().equals(entry.getKey())) {
                            fichajesUpdate.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (!fichajes.isEmpty()) {
                        databaseReferenceUsuarios.child(fichaje.getIDUsuario()).child("Fichajes").removeValue().addOnCompleteListener(task1 ->
                                databaseReferenceUsuarios.child(fichaje.getIDUsuario()).child("Fichajes").updateChildren(fichajesUpdate).addOnCompleteListener(task11 ->
                                        databaseReferenceFichaje.child(fichaje.getIdFichaje()).removeValue().addOnCompleteListener(listener)));
                    } else {
                        databaseReferenceUsuarios.child(fichaje.getIDUsuario()).child("Fichajes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReferenceAusencia.child(fichaje.getIdFichaje()).removeValue().addOnCompleteListener(listener);
                            }
                        });
                    }
                }
            }
        });
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

    public void borrarAusencia(Ausencia ausencia, OnCompleteListener<Void> listener) {
        getInfoUser(ausencia.getIdUsuario(), task -> {
            if (task.isSuccessful()) {
                //Accedemos al mapa ausencias del nodo del usuario
                Map<String, Object> ausencias = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get("Ausencias");
                Map<String, Object> ausenciasUpdate = new HashMap<>();
                if (ausencias != null) {
                    //Nos guardamos las referencias de los ausencias del usuario
                    for (Map.Entry<String, Object> entry : ausencias.entrySet()) {
                        if (!ausencia.getIdAusencia().equals(entry.getKey())) {
                            ausenciasUpdate.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (!ausencias.isEmpty()) {
                        databaseReferenceUsuarios.child(ausencia.getIdUsuario()).child("Ausencias").removeValue().addOnCompleteListener(task1 ->
                                databaseReferenceUsuarios.child(ausencia.getIdUsuario()).child("Ausencias").updateChildren(ausenciasUpdate).addOnCompleteListener(task11 ->
                                        databaseReferenceAusencia.child(ausencia.getIdAusencia()).removeValue().addOnCompleteListener(listener)));
                    } else {
                        databaseReferenceUsuarios.child(ausencia.getIdUsuario()).child("Ausencias").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReferenceAusencia.child(ausencia.getIdAusencia()).removeValue().addOnCompleteListener(listener);
                            }
                        });
                    }
                }
            }
        });
    }

    public void borrarAusenciaNotificaciones(Ausencia ausencia, OnCompleteListener<Void> listener) {
        getInfoUser(ausencia.getIdUsuario(), task -> {
            if (task.isSuccessful()) {
                //Accedemos al mapa ausencias del nodo del usuario
                Map<String, Object> ausencias = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get("Ausencias");
                Map<String, Object> ausenciasUpdate = new HashMap<>();
                if (ausencias != null) {
                    //Nos guardamos las referencias de los ausencias del usuario
                    String ausenciaParaBorrar = null;
                    for (Map.Entry<String, Object> entry : ausencias.entrySet()) {
                        if (!ausencia.getIdAusencia().equals(entry.getKey())) {
                            ausenciasUpdate.put(entry.getKey(), entry.getValue());
                            ausenciaParaBorrar = entry.getKey();
                        }
                    }
                    if (!ausencias.isEmpty()) {
                        String finalAusenciaParaBorrar = ausenciaParaBorrar;
                        databaseReferenceUsuarios.child(ausencia.getIdUsuario()).child("Ausencias").removeValue().addOnCompleteListener(task1 ->
                                databaseReferenceAusencia.child(finalAusenciaParaBorrar).removeValue().addOnCompleteListener(listener));
                    } else {
                        databaseReferenceUsuarios.child(ausencia.getIdUsuario()).child("Ausencias").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReferenceAusencia.child(ausencia.getIdAusencia()).removeValue().addOnCompleteListener(listener);
                            }
                        });
                    }
                }
            }
        });
    }
    //Fin Zona Ausencia

    //Inicio zona Anuncio
    public void saveAnuncio(String idRandom, Anuncio anuncio) {
        databaseReferenceAnuncio.child(idRandom).setValue(anuncio);
    }

    public void getInfoAnuncios(OnCompleteListener<DataSnapshot> listener) {
        databaseReferenceAnuncio
                .get()
                .addOnCompleteListener(listener);
    }

    public void borrarAnuncio(Anuncio anuncio, OnCompleteListener<Void> listener) {
        getInfoAnuncios(task -> {
            if (task.isSuccessful()) {
                //Accedemos al mapa ausencias del nodo del usuario
                Map<String, Object> datosAnuncio = (Map<String, Object>) ((Map<String, Object>) task.getResult().getValue()).get(anuncio.getId());
                if (!datosAnuncio.isEmpty()) {
                    databaseReferenceAnuncio.child(anuncio.getId()).removeValue().addOnCompleteListener(listener);
                }
            }
        });
    }
    //Fin zona Anuncio
}
