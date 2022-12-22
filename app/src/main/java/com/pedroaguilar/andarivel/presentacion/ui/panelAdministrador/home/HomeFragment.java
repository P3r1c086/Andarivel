package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.net.ParseException;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Fragmento que contiene los botones de fichar y salir.
 */
public class HomeFragment extends Fragment implements HomeView {

    //Creación de nuestro objeto presentador
    private final HomePresenter presenter = new HomePresenter();

    private FragmentHomeBinding binding;
    private String start_date = "";
    private String end_date = "";

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
            SimpleDateFormat countformat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            start_date = countformat1.format(Calendar.getInstance().getTime().getTime());
            setEstadoFinalJornada();
            presenter.almacenarFechaYhoraInicial(
                    binding.fechaEntrada.getText().toString(),
                    binding.horaEntrada.getText().toString());
        });
        binding.btFinalJornada.setOnClickListener(v -> {
            SimpleDateFormat countformat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            end_date = countformat2.format(Calendar.getInstance().getTime().getTime());
            setEstadoInicioJornada();
            presenter.almacenarFechaYhoraFinal(binding.horaSalida.getText().toString(), findDifference(start_date, end_date));

            binding.tvTiempoTrabajado.setText(findDifference(start_date, end_date));
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

    // Function to print difference in time start_date and end_date
    static String findDifference(String start_date, String end_date) {

        String resultado = null;

        // SimpleDateFormat converts the string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Try Block
        try {
            // parse method is used to parse the text from a string to produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference in milliseconds
            long difference_In_Time = d2.getTime() - d1.getTime();

            // Calucalte time difference in seconds, minutes, hours, years, and days
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

            //long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

            //long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            resultado = (/* difference_In_Years + " years, " + difference_In_Days + " days, "
                    + */difference_In_Hours + " hours, " + difference_In_Minutes + " minutes, "
                    + difference_In_Seconds + " seconds");
        }
        // Catch the Exception
        catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}

