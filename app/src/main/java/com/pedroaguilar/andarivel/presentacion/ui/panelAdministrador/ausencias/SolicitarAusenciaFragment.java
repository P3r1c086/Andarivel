package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentSolicitarAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SolicitarAusenciaFragment extends Fragment {
    private FragmentSolicitarAusenciaBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private Usuario usuario = new Usuario();


    public SolicitarAusenciaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSolicitarAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Asociar un evento de clic al EditText para mostrar un dialog con un calendario y poder
        //seleccionar una fecha y tomar el dato.
        binding.etFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaInicioDatePickerDialog();
            }
        });
        //Asociar un evento de clic al EditText para mostrar un dialog con un calendario y poder
        //seleccionar una fecha y tomar el dato.
        binding.etFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaFinDatePickerDialog();
            }
        });
        setListeners();

    }

    private void setListeners() {
        //Comprueba la opcion seleccionada en el spinner y obtiene el dato.
        binding.spMotivoAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elementoSeleccionado = (String) parent.getItemAtPosition(position);
                usuario.setMotivoAusencia(elementoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.btSolicitar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Se validan los datos antes de generar una ausencia.
                if (validarDatosSolicitarAunsecia()) {
                    //La descripcion no esta contemplada como obligatoria. En caso de no poner nada se le asigna un
                    //valor por defecto.
                    if (binding.etDescripcion.getText().toString().isEmpty()) {
                        usuario.setDescripcionAusencia("No especificado");
                    } else {
                        usuario.setDescripcionAusencia(binding.etDescripcion.getText().toString());
                    }
                    database.getAusencias(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
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
                                        if (!findPending){
                                            introducirDatosEnBd();
                                            almacenarDatosAusencia();
                                        } else {
                                            Toast.makeText(requireContext(), R.string.ausencia_pendiente, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }
                    });
                }


            }
        });
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
    private void showFechaInicioDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque enero es cero
                final String selectedDateInicio = day + " / " + (month + 1) + " / " + year;
                binding.etFechaInicio.setText(selectedDateInicio);
                usuario.setFechaInicioAusencia(selectedDateInicio);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
    private void showFechaFinDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDateFin = day + " / " + (month + 1) + " / " + year;
                binding.etFechaFin.setText(selectedDateFin);
                usuario.setFechaFinAusencia(selectedDateFin);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Metodo para comprobar si hay ausencias creadas. Si no las hay, numera la primera con un uno
     * y si ya hay alguna las cuenta y suma un al resultado.
     */
    private void almacenarDatosAusencia() {
        database.cuentaAusencia(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() == null) {
                        crearNodoAusencia("1");
                    } else {
                        crearNodoAusencia((((Map<String, Object>) task.getResult().getValue()).entrySet().size() + 1) + "");
                    }
                } else {

                }
            }
        });

    }

    /**
     * Metodo para crear un nodo ausencia y colocar sus datos correspondientes en su interior.
     * Se le pasa por parametro el numero, en String, obtenido en el metodo almacenarDatosAusencia.
     */
    public void crearNodoAusencia(String nNodo) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Ausencia" + nNodo + "/motivoAusencia", usuario.getMotivoAusencia());
        childUpdates.put("/Ausencia" + nNodo + "/fechaInicio", binding.etFechaInicio.getText().toString());
        childUpdates.put("/Ausencia" + nNodo + "/fechaFin", binding.etFechaFin.getText().toString());
        childUpdates.put("/Ausencia" + nNodo + "/descripcion", binding.etDescripcion.getText().toString());
        childUpdates.put("/Ausencia" + nNodo + "/usuario", mAuth.getUid());
        childUpdates.put("/Ausencia" + nNodo + "/estado", "Pendiente");
        database.actualizarAusencia(childUpdates, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Se crea un nodo ausencia n con valor true en los datos del usuario que la ha cerado para relacionar
                //la ausencia creada con el usuario que la ha creado.
                childUpdates.clear();
                childUpdates.put("/Ausencias/Ausencia" + nNodo, true);
                database.actualizarDatosUsuario(mAuth.getUid(), childUpdates, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireContext(), R.string.solicitar_ausencia_creacion_exito_mensaje, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


    private void introducirDatosEnBd() {
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setMotivoAusencia(usuario.getMotivoAusencia());
        user.setFechaInicioAusencia(usuario.getFechaInicioAusencia());
        user.setFechaFinAusencia(usuario.getFechaFinAusencia());
        user.setDescripcionAusencia(usuario.getDescripcionAusencia());
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Se validan los datos que introduce el usuario al crear una solicitud de ausencia.
     * @return devuelve true si estan bien rellenado y false si no lo estan.
     */
    private boolean validarDatosSolicitarAunsecia() {

        boolean resultado = true;

        if (usuario.getMotivoAusencia() == null || usuario.getMotivoAusencia().isEmpty()) {
            showError(getString(R.string.toast_error_tipo_ausencia));
            resultado = false;
        } else if (usuario.getFechaInicioAusencia() == null || usuario.getFechaInicioAusencia().isEmpty()) {
            showError(getString(R.string.toast_error_fecha_inicio_ausencia));
            resultado = false;
        } else if (usuario.getFechaFinAusencia() == null || usuario.getFechaFinAusencia().isEmpty()) {
            showError(getString(R.string.toast_error_fecha_fin_ausencia));
            resultado = false;
        }
        return resultado;
    }
}