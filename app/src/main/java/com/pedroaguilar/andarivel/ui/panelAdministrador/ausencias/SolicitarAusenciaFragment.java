package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.databinding.FragmentSolicitarAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.Calendar;

public class SolicitarAusenciaFragment extends Fragment {
    private FragmentSolicitarAusenciaBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
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
                //todo:falta pasarlo a la base de datos
                introducirDatosEnBd();
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
                //almacenarFechaInicio();
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
                //almacenarFechaFin();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void introducirDatosEnBd(){
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setMotivoAusencia(usuario.getMotivoAusencia());
        user.setFechaInicioAusencia(usuario.getFechaInicioAusencia());
        user.setFechaFinAusencia(usuario.getFechaFinAusencia());
        user.setDescripcionAusencia(usuario.getDescripcionAusencia());
        databaseReference.child(Constantes.TABLA_AUSENCIAS).child(user.getID()).setValue(user);
    }
}
