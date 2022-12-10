package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentEstadoTrabajadoresBinding;
import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.informes.AdaptadorInformes;

import java.util.ArrayList;

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
        binding.rvFichajesDiarios.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.fichajesCurrentUsers();
        //compareDate();
    }

    @Override
    public void agnadirListaUsuarios(ArrayList<Fichaje> list) {
        //Colocamos en el adaptador la lista de fichajes que continen todos los datos necesarios.
        binding.rvFichajesDiarios.setAdapter(new AdaptadorInformes(list));
    }

    @Override
    public void mostrarListaVacia() {
        ArrayList<Fichaje> listaConVacio = new ArrayList<>();
        Fichaje fVacio = new Fichaje();
        //En caso de que no haya fichajes todavia, notificamos al administrador que no hay fichajes.
        fVacio.setNombreUsuario(getString(R.string.no_hay_fichajes));
        listaConVacio.add(fVacio);
        binding.rvFichajesDiarios.setAdapter(new AdaptadorInformes(listaConVacio));
    }

    @Override
    public void mostrarFalloFirebase() {
        Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
    }

    //    @Override
//    public String setInicioJornada() {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        String date1 = null;
//        try {
//
//            String hora = format.parse("19:00:00").toString();
//            SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//            date1 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + hora;
//            binding.tvFecha1.setText(date1);
//        } catch (Exception e) {
//            Log.i("///////", "error hora2 parse");
//        }
//        return date1;
//    }
//
//    @Override
//    public String setFinalJornada() {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        String date2 = null;
//        try {
//
//            String hora = format.parse("19:00:00").toString();
//            SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//            date2 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + hora;
//            binding.tvFecha2.setText(date2);
//        } catch (Exception e) {
//            Log.i("///////", "error hora2 parse");
//        }
//
//        return date2;
//    }
//
//    @Override
//    public void compareDate() {
//        String date1 = setInicioJornada();
//        String date2 = setFinalJornada();
//        binding.btnOk.setOnClickListener(v -> {
//            setFinalJornada();
//            if (date1.compareTo(date2) == 0) {
//                binding.tvResult.setText("La fechas son iguales");
//            } else if (date1.compareTo(date2) < 0) {
//                binding.tvResult.setText("La fecha1 es menor");
//            } else if (date1.compareTo(date2) > 0) {
//                binding.tvResult.setText("La fecha1 es mayor");
//            }
//        });
////todo:no quiero saber si es mayor o menor, sino cuando se sale de la franja de tiempo del dia actual
//    }
//    @Override
//    public String setInicioJornada() {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//        String date1 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime());
//        binding.tvFecha1.setText(dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime()));
//    return date1;
//}
//
//    @Override
//    public String setFinalJornada() {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//        String date2 = dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime());
//        binding.tvFecha2.setText(dateformat.format(Calendar.getInstance().getTime().getTime()) + " " + format.format(Calendar.getInstance().getTime().getTime()));
//        return date2;
//    }
//
//    @Override
//    public void compareDate() {
//        String date1 = setInicioJornada();
//        binding.btnOk.setOnClickListener(v -> {
//            setFinalJornada();
//            if (date1.compareTo(setFinalJornada()) == 0) {
//                binding.tvResult.setText("La fechas son iguales");
//            } else if (date1.compareTo(setFinalJornada()) < 0) {
//                binding.tvResult.setText("La fecha1 es menor");
//            } else if (date1.compareTo(setFinalJornada()) > 0) {
//                binding.tvResult.setText("La fecha1 es mayor");
//            }
//        });
////todo:no quiero saber si es mayor o menor, sino cuando se sale de la franja de tiempo del dia actual
//    }
}
