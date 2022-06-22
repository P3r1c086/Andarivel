package com.pedroaguilar.andarivel.presentacion.ui.login;

import androidx.core.util.PatternsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.regex.Pattern;

public class NuevoUsuarioPresenter extends BasePresenter<NuevoUsuarioView> {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void botonAceptarClickado(String nombreReal, String apellido, String direcion, String correo, String telefon, String mail, String pass) {
        if (validarCampos(nombreReal, apellido, direcion, correo, telefon, pass) && validateEmail(mail) && validatePassword(pass)) {
            createUserWithEmailAndPassword(nombreReal, apellido, direcion, correo, telefon, mail, pass);
        }
    }

    public boolean validateEmail(String email) {
        boolean resultado = true;
        if (email.isEmpty()) {
            view.setErrorEmail();
            resultado = false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showErrorEmail();
            resultado = false;
        }
        return resultado;
    }

    public boolean validatePassword(String password) {
        boolean resultado = true;
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

        if (password.isEmpty()) {
            view.showErrorDebesIntroducirContrasena();
            resultado = false;
        } else if (!passwordRegex.matcher(password).matches()) {
            view.showErrorContrasenaDebil();
            resultado = false;
        }
        return resultado;
    }

    public boolean validarTelefono(String tlf) {
        boolean resultado = true;
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
            view.showErrorTlfNoValido();
            resultado = false;
        }
        return resultado;
    }

    public boolean validarCampos(String nombreReal, String apellido, String direcion, String correo, String telefon, String pass) {
        boolean resultado = true;

        if (nombreReal.isEmpty()) {
            view.mostrarErrorRequeridoNombre();

            resultado = false;
        } else if (apellido.isEmpty()) {
            view.mostrarErrorRequeridoApellido();
            resultado = false;
        } else if (direcion.isEmpty()) {
            view.mostrarErrorRequeridoDireccion();
            resultado = false;
        } else if (correo.isEmpty()) {
            view.mostrarErrorRequeridoCorreo();
            resultado = false;
        } else if (telefon.isEmpty()) {
            view.mostrarErrorRequeridoTelefono();
            resultado = false;

        } else if (pass.isEmpty()) {
            view.mostrarErrorRequeridoPass();
            resultado = false;
        }

        if (!telefon.isEmpty()) {
            resultado = validarTelefono(telefon);

        }
        return resultado;
    }

    public void createUserWithEmailAndPassword(String nombreReal, String apellido, String direcion, String correo, String telefon, String mail, String pass) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && (task.getResult().getUser() != null)) {
                        crearUsuarioYEscribirEnBaseDeDatos(task.getResult().getUser().getUid(), nombreReal, apellido, direcion, mail, telefon, pass);
                        view.navegarActividadMenu();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            view.toastEmailYaRegistrado();
                        } else {
                            view.falloAutenticacion();
                        }
                    }
                });
    }

    public void crearUsuarioYEscribirEnBaseDeDatos(String firebaseAuthUsuarioId, String nombre, String apellidos, String direccion, String email, String telefono, String password) {
        Usuario user = new Usuario();
        user.setID(firebaseAuthUsuarioId);
        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setDireccion(direccion);
        user.setEmail(email);
        user.setTelefono(telefono);
        user.setPassword(password);
        user.setEsAdiminstrador(false);
        database.crearUsuario(firebaseAuthUsuarioId, user);
    }
}
