package com.pedroaguilar.andarivel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import java.util.regex.Pattern;

public class NuevoUsuario extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText email;
    private EditText nombreUsuario;
    private EditText password;
    private Button registrar;
    private Button atras;
    BaseDatosFirebase bd = new BaseDatosFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        registrar = findViewById(R.id.btRegistrarse);
        atras = findViewById(R.id.btAtras);
        Intent i = new Intent(this, Login.class);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail() == true && validatePassword() == true){
                    obtenerDatosUsuario();
                    bd.escribirEnBd();
                    startActivity(i);
                }
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    public void obtenerDatosUsuario(){
        nombre = findViewById(R.id.etNombreReal);
        apellidos = findViewById(R.id.etApellidos);
        direccion = findViewById(R.id.etDireccion);
        email = findViewById(R.id.etEmail);
        nombreUsuario = findViewById(R.id.etNombre);
        password = findViewById(R.id.etPass);
        registrar = findViewById(R.id.btRegistrarse);


        Intent i = new Intent(this,BaseDatosFirebase.class);
        i.putExtra("nombre",nombre.getText());
        i.putExtra("apellidos",apellidos.getText());
        i.putExtra("direccion",direccion.getText());
        i.putExtra("email",email.getText());
        i.putExtra("nombreUsuario", nombreUsuario.getText());
        i.putExtra("pass",password.getText());
        startActivity(i);
        //TODO: pasar estos datos a la clase Json

    }
    private Boolean validateEmail()  {
        Boolean resultado = false;
        email = findViewById(R.id.etEmail);
        //Recuperamos el contenido del textInputLayout
        String correo = null;
        correo = email.getText().toString();
        if(correo.isEmpty()){
            Toast.makeText(this,"Debes introducir un email", Toast.LENGTH_LONG).show();
            resultado = false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Debes introducir un email válido", Toast.LENGTH_LONG).show();
            resultado = false;
        }else{
            resultado = true;
        }
        return resultado;
    }
    private Boolean validatePassword() {
        Boolean resultado = false;
        password = findViewById(R.id.etPass);
        //Recuperamos el contenido del textInputLayout
        String contrasena = null;
        contrasena = password.getText().toString();

        // Patrón con expresiones regulares
        Pattern passwordRegex = Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //al menos un dígito
                        "(?=.*[a-z])" +        //al menos 1 letra minúscula
                        "(?=.*[A-Z])" +        //al menos 1 letra mayúscula
                        //"(?=.*[@#$%^&+=])" +    //al menos 1 carácter especial
                        "(?=\\S+$)" +           //sin espacios en blanco
                        ".{6,}" +               //al menos 6 caracteres
                        "$"
        );

        if (contrasena.isEmpty()){
            Toast.makeText(this,"Debes introducir una contraseña", Toast.LENGTH_LONG).show();
            resultado = false;
        }else if (!passwordRegex.matcher(contrasena).matches()){
            Toast.makeText(this,"La contraseña es demasiado débil", Toast.LENGTH_LONG).show();
            resultado = false;
        }else{
            resultado = true;
        }
        return resultado;
    }
}
