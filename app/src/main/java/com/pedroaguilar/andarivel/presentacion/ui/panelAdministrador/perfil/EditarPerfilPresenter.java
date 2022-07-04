package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditarPerfilPresenter extends BasePresenter<EditarPerfilView> {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void clickBotonAceptar(String nombre, String apellido, String direccion, String telefono){
        if(validarCampos(nombre,apellido,direccion,telefono)){
            //Creamos un mapa donde colocar los nuevos datos que introducira el usuario
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/nombre/", nombre);
            childUpdates.put("/apellidos/", apellido);
            childUpdates.put("/direccion/", direccion);
            childUpdates.put("/telefono/", telefono);
            database.actualizarDatosUsuario(auth.getCurrentUser().getUid() , childUpdates , task -> { });
            view.navegarAperfil();
            view.showToastActualizadoCorrectamente();
        }
    }

    private boolean validarTelefono(String tlf) {
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
            view.showTelefonoNoValido();
            resultado = false;
        }
        return resultado;
    }

    private boolean validarCampos(String nombre, String apellido, String direccion, String telefono) {
        boolean resultado = true;

        if (nombre.isEmpty()) {
            view.mostrarErrorRequeridoNombre();
            resultado = false;
        } else if (apellido.isEmpty()) {
            view.mostrarErrorRequeridoApellido();
            resultado = false;
        } else if (direccion.isEmpty()) {
            view.mostrarErrorRequeridoDireccion();
            resultado = false;
        } else if (telefono.isEmpty()) {
            view.mostrarErrorRequeridoTelefono();
            resultado = false;
        }
        if (!telefono.isEmpty()) {
            resultado = validarTelefono(telefono);

        }
        return resultado;
    }

}


