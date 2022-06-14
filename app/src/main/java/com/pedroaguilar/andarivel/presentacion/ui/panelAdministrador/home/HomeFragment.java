package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragmento que contiene los botones de fichar y salir.
 */
public class HomeFragment extends Fragment implements HomeView {

    //Creación de nuestro objeto presentador
    private final HomePresenter presenter = new HomePresenter();

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //Al inflar la vista se pone la imagen del hombre con casco invisible.
        binding.imgEstado2.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Asignacion de nuestra vista al presentador
        presenter.initialize(this);
        setListeners();
        presenter.getFichajes();
    }

    private void setListeners() {
        binding.btFichar.setOnClickListener(v -> {
            setEstadoFinalJornada();
            presenter.almacenarFechaYhoraInicial(
                    binding.fechaEntrada.getText().toString(),
                    binding.horaEntrada.getText().toString());
        });
        binding.btFinalJornada.setOnClickListener(v -> {
            setEstadoInicioJornada();
            presenter.almacenarFechaYhoraFinal(binding.horaSalida.getText().toString());
        });
    }

    @Override
    public void setEstadoInicioJornada(){
        binding.btFichar.setVisibility(View.VISIBLE);
        binding.btFinalJornada.setVisibility(View.INVISIBLE);
        binding.imgEstado2.setVisibility(View.INVISIBLE);
        binding.imgEstado1.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        binding.horaSalida.setText("" + format.format(Calendar.getInstance().getTime().getTime()));
        binding.fechaSalida.setText(dateformat.format(Calendar.getInstance().getTime().getTime()));
    }
    @Override
    public void setEstadoFinalJornada(){
        binding.btFichar.setVisibility(View.INVISIBLE);
        binding.btFinalJornada.setVisibility(View.VISIBLE);
        binding.imgEstado1.setVisibility(View.INVISIBLE);
        binding.imgEstado2.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        binding.horaEntrada.setText("" + format.format(Calendar.getInstance().getTime()));
        binding.fechaEntrada.setText(dateformat.format(Calendar.getInstance().getTime()));
        binding.horaSalida.setText("");
        binding.fechaSalida.setText("");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        presenter.dispose();
    }

}

