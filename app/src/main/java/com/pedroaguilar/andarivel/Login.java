package com.pedroaguilar.andarivel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private EditText etNombre;
    private EditText etPassword;
    private Button btRegistrar;
    private String nombre;
    private String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registrar();

    }

    private void clickBotonEntrar(){
        etNombre = findViewById(R.id.etNombre);
        etPassword = findViewById(R.id.etPass);
        nombre = etNombre.getText().toString();
        pass = etPassword.getText().toString();





        
    }

    private void registrar(){
        btRegistrar = findViewById(R.id.btIrRegistro);
        Intent i = new Intent(this,NuevoUsuario.class);
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }
}
