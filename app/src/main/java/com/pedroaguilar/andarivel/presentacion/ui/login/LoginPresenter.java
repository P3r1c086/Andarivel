package com.pedroaguilar.andarivel.presentacion.ui.login;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginView> {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void signInWithEmailAndPassword(String email, String password) {
        //Firebase te envia la respuseta en un objeto tipo Task
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        view.navegarActividadMenu();
                    } else {
                        view.showErrorAutenticarFallo();
                    }
                });
    }
}
