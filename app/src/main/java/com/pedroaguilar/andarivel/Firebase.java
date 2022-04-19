package com.pedroaguilar.andarivel;

import android.util.Log;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class Firebase {

    public void incrementCounter() {//Método para incrementar en 1 el id de la base de datos
        firebase.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    Log.d("Firebase counter increment failed.");
                } else {
                    Log.d("Firebase counter increment succeeded.");
                }
            }
        });
    }
    public void autenticar(){//Método para autenticar el email y la contraseña

    }
}
