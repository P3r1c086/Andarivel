package com.pedroaguilar.andarivel.servicios;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class ServicioFirebase {

    private final FirebaseDatabase firebaseDataBase =  FirebaseDatabase.getInstance();

    public void incrementCounter() {
        //Método para incrementar en 1 el id de la base de datos
        firebaseDataBase.getReference("Usuarios").runTransaction(new Transaction.Handler() {
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
                    Log.d(ServicioFirebase.class.getSimpleName(), "Firebase counter increment failed.");
                } else {
                    Log.d(ServicioFirebase.class.getSimpleName(), "Firebase counter increment succeeded.");
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
