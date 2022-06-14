package com.pedroaguilar.andarivel.presentacion.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.PatternsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.PanelAdministradorActivity;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.regex.Pattern;

/**
 * Fragmento para que el usuario intrduzca la informacion para crear un nuevo usuario.
 * Al crear el nuevo usuario, si toddo esta bien, navegara a la actividad de PanelAdiminstrador
 */
public class NuevoUsuarioFragment extends Fragment {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText email;
    private EditText telefono;
    private EditText password;
    private Button aceptar;
    private Boolean esAdministrador = false;


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
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPass);
        nombre = view.findViewById(R.id.etNombreReal);
        apellidos = view.findViewById(R.id.etApellidos);
        direccion = view.findViewById(R.id.etDireccion);
        telefono = view.findViewById(R.id.etTelefono);
        aceptar = view.findViewById(R.id.btAceptar);

        setListeners();
    }

    private void setListeners(){
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if(validarCampos()){
                        if (validateEmail() && validatePassword()) {

                            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful() && (task.getResult().getUser() != null)) {

                                                    crearUsuarioYEscribirEnBaseDeDatos(task.getResult().getUser().getUid());

                                                    //Navegamos a la nueva actividad y matamos esta para que no exista navegacion a ella de nuevo
                                                    startActivity(new Intent(getContext(), PanelAdministradorActivity.class));
                                                    getActivity().finish();


                                            } else {
                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(getContext(), "Este email ya está registrado.", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getContext(), "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        }
                                    });
                        }
                    }

                }
            }
        });

    }

    private boolean validateEmail() {
        boolean resultado = true;
        //Recuperamos el contenido del textInputLayout
        String correo = email.getText().toString();
        if (correo.isEmpty()) {
            email.setError("Debes introducir un email");
            resultado = false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()) {
            showError("Debes introducir un email válido");
            resultado = false;
        }
        return resultado;
    }

    private void showError(String message) {
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
                        ".{6,}" +               //al menos 6 caracteres
                        "$"
        );

        if (contrasena.isEmpty()) {
            showError("Debes introducir una contraseña");
            resultado = false;
        } else if (!passwordRegex.matcher(contrasena).matches()) {
            showError("La contraseña es demasiado débil");
            resultado = false;
        }
        return resultado;
    }
    private boolean validarTelefono() {
        boolean resultado = true;
        //Recuperamos el contenido del textInputLayout
        String tlf = telefono.getText().toString();
        // Patrón con expresiones regulares
        Pattern tlfRegex = Pattern.compile(
                /**
                 * Estos son los formatos que se van a validar
                 * "2055550125","202 555 0125", "(202) 555-0125", "+111 (202) 555-0125",
                 * "636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89"
                 */

                "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"
                        + ".{9,19}$"//longitud entre 9 y 19 caracteres
        );
        if (!tlfRegex.matcher(tlf).matches()) {
            showError("El teléfono no es válido");
            resultado = false;
        }
        return resultado;
    }

    private boolean validarCampos() {
        boolean resultado = true;
        String nombreReal = nombre.getText().toString();
        String apellido = apellidos.getText().toString();
        String direcion = direccion.getText().toString();
        String telefon = telefono.getText().toString();
        String correo = email.getText().toString();
        String pass = password.getText().toString();

        if (nombreReal.isEmpty()) {
            nombre.setError("Requerido");
            resultado = false;
        } else if (apellido.isEmpty()) {
            apellidos.setError("Requerido");
            resultado = false;
        } else if (direcion.isEmpty()) {
            direccion.setError("Requerido");
            resultado = false;
        } else if (correo.isEmpty()) {
            email.setError("Requerido");
            resultado = false;
        } else if (telefon.isEmpty()) {
            telefono.setError("Requerido");
            resultado = false;

        } else if (pass.isEmpty()) {
            password.setError("Requerido");
            resultado = false;
        }

        if (!telefon.isEmpty()) {
            resultado = validarTelefono();

        }
        return resultado;
    }

    public void crearUsuarioYEscribirEnBaseDeDatos(String firebaseAuthUsuarioId) {
        Usuario user = new Usuario();
        user.setID(firebaseAuthUsuarioId);
        user.setNombre(nombre.getText().toString());
        user.setApellidos(apellidos.getText().toString());
        user.setDireccion(direccion.getText().toString());
        user.setEmail(email.getText().toString());
        user.setTelefono(telefono.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEsAdiminstrador(esAdministrador);
        database.crearUsuario(firebaseAuthUsuarioId, user);
    }

}