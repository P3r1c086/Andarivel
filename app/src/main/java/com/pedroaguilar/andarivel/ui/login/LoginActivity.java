package com.pedroaguilar.andarivel.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pedroaguilar.andarivel.R;

/**
 * Primera actividad que se inicia en la app. Su layout es inflado, al contener un FragmentContainerView
 * el que a su vez contiene un grafo de navegacion donde se indica el primer fragmento que tiene que inflarse
 * (SplashFragment - marcado con una casita en el grafo de navegacion)
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}