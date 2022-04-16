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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.BaseDatosFirebase;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.Usuario;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;


public class NuevoUsuarioFragment extends Fragment {

    private final FirebaseAuth mAuth =  FirebaseAuth.getInstance();

    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText email;
    private EditText nombreUsuario;
    private EditText password;
    private Button registrar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    BaseDatosFirebase bd = new BaseDatosFirebase();
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
        BaseDatosFirebase bd = new BaseDatosFirebase();
        email =  view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPass);
        nombre = view.findViewById(R.id.etNombreReal);
        apellidos = view.findViewById(R.id.etApellidos);
        direccion = view.findViewById(R.id.etDireccion);
        nombreUsuario = view.findViewById(R.id.etNombre);
        Button aceptar = view.findViewById(R.id.btAceptar);


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (validateEmail() && validatePassword()) {
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            introducirDatos();
                                            Navigation.findNavController(v).navigate(R.id.panelAdministrador);
                                        } else {
                                            Toast.makeText(getContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    private boolean validateEmail()  {
        boolean resultado = true;
        //Recuperamos el contenido del textInputLayout
        String correo = email.getText().toString();
        if(correo.isEmpty()){
            email.setError("Debes introducir un email");
            resultado = false;
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            showError("Debes introducir un email válido");
            resultado = false;
        }
        return resultado;
    }

    private void showError(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean validatePassword() {
        boolean resultado = true;
        //Recuperamos el contenido del textInputLayout
        String contrasena = password.getText().toString();
        // Patrón con expresiones regulares
        Pattern passwordRegex = Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //al menos un dígito
                        "(?=.*[a-z])" +        //al menos 1 letra minúscula
                        "(?=.*[A-Z])" +        //al menos 1 letra mayúscula
                        "(?=\\S+$)" +           //sin espacios en blanco
                        ".{4,}" +               //al menos 4 caracteres
                        "$"
        );

        if (contrasena.isEmpty()){
            showError("Debes introducir una contraseña");
            resultado = false;
        }else if (!passwordRegex.matcher(contrasena).matches()){
            showError("La contraseña es demasiado débil");
            resultado = false;
        }
        return resultado;
    }
    private boolean validarCampos(){
        boolean resultado = true;
        String nombreReal = nombre.getText().toString();
        String apellido = apellidos.getText().toString();
        String direcion = direccion.getText().toString();
        String nombreUser = nombreUsuario.getText().toString();
        if(nombreReal.isEmpty()){
            nombre.setError("Requerido");
            resultado = false;
        }else if(apellido.isEmpty()){
            apellidos.setError("Requerido");
            resultado = false;
        }else if(direcion.isEmpty()){
            direccion.setError("Requerido");
            resultado = false;
        }else if(nombreUser.isEmpty()){
            nombreUsuario.setError("Requerido");
            resultado = false;
        }
        return resultado;
    }

    public void inicializarFirebase(){
        //Todo: Cambiar inzializacion a Application
        FirebaseApp.initializeApp(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void introducirDatos(){
        inicializarFirebase();
        Usuario user = new Usuario();
        user.setID(UUID.randomUUID().toString());
        user.setNombre(nombre.getText().toString());
        user.setApellidos(apellidos.getText().toString());
        user.setDireccion(direccion.getText().toString());
        user.setEmail(email.getText().toString());
        user.setNombreUsuario(nombreUsuario.getText().toString());
        user.setPassword(password.getText().toString());
        databaseReference.child("Usuarios").child(user.getID()).setValue(user);
    }
}