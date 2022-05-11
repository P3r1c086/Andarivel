package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentAusenciasBinding;

import java.util.Calendar;

public class AusenciasFragment extends Fragment {
    private FragmentAusenciasBinding binding;

    public AusenciasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAusenciasBinding.inflate(inflater, container, false);
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
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
    }
}