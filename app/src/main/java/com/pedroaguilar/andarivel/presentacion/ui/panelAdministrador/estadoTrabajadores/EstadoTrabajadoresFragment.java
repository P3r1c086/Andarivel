package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentEstadoTrabajadoresBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EstadoTrabajadoresFragment extends Fragment implements EstadoTrabajadoresView {
    private FragmentEstadoTrabajadoresBinding binding;
    private final EstadoTrabajadoresPresenter presenter = new EstadoTrabajadoresPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEstadoTrabajadoresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);

        compareDate();
    }

    @Override
    public String setInicioJornada() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        String date1 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime());
        binding.tvFecha1.setText(dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime()));
        return date1;
    }

    @Override
    public String setFinalJornada() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        String date2 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime());
        binding.tvFecha2.setText(dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime()));
        return date2;
    }

    @Override
    public void compareDate() {
        String date1 = setInicioJornada();
        binding.btnOk.setOnClickListener(v -> {
            setFinalJornada();
            if (date1.compareTo(setFinalJornada()) == 0) {
                binding.tvResult.setText("La fechas son iguales");
            } else if (date1.compareTo(setFinalJornada()) < 0) {
                binding.tvResult.setText("La fecha1 es menor");
            } else if (date1.compareTo(setFinalJornada()) > 0) {
                binding.tvResult.setText("La fecha1 es mayor");
            }
        });
//todo:no quiero saber si es mayor o menor, sino cuando se sale de la franja de tiempo del dia actual
    }
}
