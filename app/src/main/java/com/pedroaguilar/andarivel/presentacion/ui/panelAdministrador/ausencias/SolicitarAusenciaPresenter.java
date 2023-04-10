package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosPresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SolicitarAusenciaPresenter extends CamaraYpermisosPresenter<SolicitarAusenciaView> {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public void botonSolicitarClickado(String descripcion, Usuario usuario) {
        if (validarDatosSolicitarAunsecia(usuario)) {
            //La descripcion no esta contemplada como obligatoria. En caso de no poner nada se le asigna un
            //valor por defecto.
            if (descripcion.isEmpty()) {
                usuario.setDescripcionAusencia("No especificado");
            } else {
                usuario.setDescripcionAusencia(descripcion);
            }
            database.getAusencias(task -> {
                //Una vez realizada la consulta a la base de datos se obtiene un objeto Task con los datos.
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() != null) {
                        //Se crea un objeto Mapa y se castean los resultado del objeto Task a tipo Mapa.
                        Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                        if (mapRaiz != null && !mapRaiz.isEmpty()) {
                            Boolean findPending = false;
                            //Recorremos el mapa con un foreach buscando un nodo que pertenezca al usuario logueado y además que
                            //el estado de la ausencia sea Pendiente.
                            for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                                Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                                if (mapAusencia.get("usuario").equals(FirebaseAuth.getInstance().getUid())
                                        && Objects.equals(mapAusencia.get("estado"), "Pendiente")) {
                                    findPending = true;
                                    break;
                                }
                            }
                            //En función de si no lo ha encontrado o sí, hace las llamadas o saca un mensaje
                            if (!findPending) {
                                usuario.setID(mAuth.getCurrentUser().getUid());
                                almacenarDatosAusencia(usuario);
                            } else {
                                view.showAusenciaPendiente();
                            }
                        }
                    } else {
                        //Caso de lista de Ausencias vacia -> Creamos la primera ausencia
                        usuario.setID(mAuth.getCurrentUser().getUid());
                        almacenarDatosAusencia(usuario);
                    }
                }
            });
        }
    }

    /**
     * Se validan los datos que introduce el usuario al crear una solicitud de ausencia.
     *
     * @return devuelve true si estan bien rellenado y false si no lo estan.
     */
    private boolean validarDatosSolicitarAunsecia(Usuario usuario) {
        boolean resultado = true;
        if (usuario.getMotivoAusencia() == null || usuario.getMotivoAusencia().isEmpty()) {
            view.showErrorMotivoAusencia();
            resultado = false;
        } else if (usuario.getFechaInicioAusencia() == null || usuario.getFechaInicioAusencia().isEmpty()) {
            view.showErrorFechaIncio();
            resultado = false;
        } else if (usuario.getFechaFinAusencia() == null || usuario.getFechaFinAusencia().isEmpty()) {
            view.showErrorFechaFin();
            resultado = false;
        }
        return resultado;
    }

    /**
     * Metodo para comprobar si hay ausencias creadas. Si no las hay, numera la primera con un uno
     * y si ya hay alguna las cuenta y suma un al resultado.
     */
    private void almacenarDatosAusencia(Usuario usuario) {
        database.cuentaAusencia(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getValue() == null) {
                    crearNodoAusencia("1", usuario);
                } else {
                    crearNodoAusencia((((Map<String, Object>) task.getResult().getValue()).entrySet().size() + 1) + "", usuario);
                }
            } else {
            }
        });
    }

    /**
     * Metodo para crear un nodo ausencia y colocar sus datos correspondientes en su interior.
     * Se le pasa por parametro el numero, en String, obtenido en el metodo almacenarDatosAusencia.
     */
    public void crearNodoAusencia(String nNodo, Usuario usuario) {
        Map<String, Object> childUpdates = new HashMap<>();
        String nombreAusencia = "Ausencia" + nNodo;
        childUpdates.put("/" + nombreAusencia + "/motivoAusencia", usuario.getMotivoAusencia());
        childUpdates.put("/" + nombreAusencia + "/fechaInicio", usuario.getFechaInicioAusencia());
        childUpdates.put("/" + nombreAusencia + "/fechaFin", usuario.getFechaFinAusencia());
        childUpdates.put("/" + nombreAusencia + "/descripcion", usuario.getDescripcionAusencia());
        childUpdates.put("/" + nombreAusencia + "/usuario", usuario.getID());
        if (imagen != null) {
            childUpdates.put("/" + nombreAusencia + "/adjunto", nombreAusencia + ".jpg");
            guardarDocumentoAusenciaEnStorage(nombreAusencia);
        }
        childUpdates.put("/" + nombreAusencia + "/estado", "Pendiente");
        database.actualizarAusencia(childUpdates, task -> {
            //Se crea un nodo ausencia n con valor true en los datos del usuario que la ha creado para relacionar
            //la ausencia creada con el usuario que la ha creado.
            childUpdates.clear();
            childUpdates.put("/Ausencias/Ausencia" + nNodo, true);
            database.actualizarDatosUsuario(mAuth.getUid(), childUpdates, task1 -> view.showAusenciaCreadaConExito());
        });
    }
}
