package com.pedroaguilar.andarivel.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.PatternsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedroaguilar.andarivel.R;

import java.util.regex.Pattern;


public class NuevoUsuarioFragment extends Fragment {

    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText email;
    private EditText nombreUsuario;
    private EditText password;
    private Button registrar;
    private Button atras;
    public NuevoUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_usuario, container, false);
    }
    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
        super.onViewCreated(view, savedInstanceState);



        Button aceptar = view.findViewById(R.id.btAceptar);
        Button atras = view.findViewById(R.id.btAtras);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(validateEmail(v) == true && validatePassword(v) == true){
                    Navigation.findNavController(v).navigate(R.id.loginFragment);
               // }

            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            }
        });
    }
    private Boolean validateEmail(@NonNull View view)  {
        Boolean resultado = false;
        email = view.findViewById(R.id.etEmail);
        //Recuperamos el contenido del textInputLayout
        String correo = null;
        correo = email.getText().toString();
        if(correo.isEmpty()){
            //Toast.makeText(this,"Debes introducir un email", Toast.LENGTH_LONG).show();
            resultado = false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            //Toast.makeText(this,"Debes introducir un email válido", Toast.LENGTH_LONG).show();
            resultado = false;
        }else{
            resultado = true;
        }
        return resultado;
    }
    private Boolean validatePassword(@NonNull View view) {
        Boolean resultado = false;
        password = view.findViewById(R.id.etPass);
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
            //Toast.makeText(this,"Debes introducir una contraseña", Toast.LENGTH_LONG).show();
            resultado = false;
        }else if (!passwordRegex.matcher(contrasena).matches()){
            //Toast.makeText(this,"La contraseña es demasiado débil", Toast.LENGTH_LONG).show();
            resultado = false;
        }else{
            resultado = true;
        }
        return resultado;
    }
}