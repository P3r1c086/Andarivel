package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

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

public class SolicitarAusenciaFragment extends Fragment {
    private FragmentSolicitarAusenciaBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private Usuario usuario = new Usuario();
    private String motivoAusencia ="";
    private String fechaI ="";
    private String fechaF ="";
    private String descripcion ="";
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


        //Asociar un evento de clic al EditText
        binding.etFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaInicioDatePickerDialog();
            }
        });
        //Asociar un evento de clic al EditText
        binding.etFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaFinDatePickerDialog();
            }
        });
        setListeners();

    }
    private void setListeners(){
        binding.spMotivoAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elementoSeleccionado = (String) parent.getItemAtPosition(position);
                usuario.setMotivoAusencia(elementoSeleccionado);
                //todo:falta pasarlo a la base de datos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        binding.btSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etDescripcion.getText().toString().isEmpty()){
                    usuario.setDescripcionAusencia("No especificado");
                }else {
                    usuario.setDescripcionAusencia(binding.etDescripcion.getText().toString());
                }
                introducirDatosEnBd();
                almacenarDatosAusencia();

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
                // +1 because January is zero
                final String selectedDateInicio = day + " / " + (month+1) + " / " + year;
                binding.etFechaInicio.setText(selectedDateInicio);
                usuario.setFechaInicioAusencia(selectedDateInicio);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
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
                final String selectedDateFin = day + " / " + (month+1) + " / " + year;
                binding.etFechaFin.setText(selectedDateFin);
                usuario.setFechaFinAusencia(selectedDateFin);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void almacenarDatosAusencia() {
        database.cuentaAusencia(new OnCompleteListener<DataSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getValue() == null){
                        crearNodoAusencia("1");
                    } else {
                        crearNodoAusencia((((Map<String, Object>)task.getResult().getValue()).entrySet().size() + 1) + "");
                    }
                } else {

                }
            }
        });

    }
    public void crearNodoAusencia(String nNodo){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Ausencia"+nNodo+"/motivoAusencia", usuario.getMotivoAusencia());//no se si ya lo tiene tomado aqui el valor
        childUpdates.put("/Ausencia"+nNodo+"/fechaInicio", binding.etFechaInicio.getText().toString());
        childUpdates.put("/Ausencia"+nNodo+"/fechaFin", binding.etFechaFin.getText().toString());
        childUpdates.put("/Ausencia"+nNodo+"/descripcion", binding.etDescripcion.getText().toString());
        childUpdates.put("/Ausencia"+nNodo+"/usuario", mAuth.getUid());
        childUpdates.put("/Ausencia"+nNodo+"/estado", "Pendiente");
        database.actualizarAusencia(childUpdates, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                childUpdates.clear();
                childUpdates.put("/Ausencias/Ausencia"+nNodo, true);
                database.actualizarDatosUsuario(mAuth.getUid(), childUpdates, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireContext(), R.string.solicitar_ausencia_creacion_exito_mensaje, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }



    private void introducirDatosEnBd(){
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setMotivoAusencia(usuario.getMotivoAusencia());
        user.setFechaInicioAusencia(usuario.getFechaInicioAusencia());
        user.setFechaFinAusencia(usuario.getFechaFinAusencia());
        user.setDescripcionAusencia(usuario.getDescripcionAusencia());
       // database.crearAusencia(user.getID(),user);
    }
}
