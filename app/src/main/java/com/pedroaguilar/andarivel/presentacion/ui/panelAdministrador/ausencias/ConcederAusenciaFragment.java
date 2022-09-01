package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentConcederAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Ausencia;

import java.util.ArrayList;

/**
 * Fragmento solo usado por el administrador para conceder o denegar ausencias.
 */
public class ConcederAusenciaFragment extends Fragment implements ConcederAusenciaView {

    private FragmentConcederAusenciaBinding binding;
    private final ConcederAusenciaPresenter presenter = new ConcederAusenciaPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConcederAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        binding.listaAusencias.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.leerTodosUsuariosDatabase();
    }


    @Override
    public void mostrarInfoNoPeticionesAusencia() {
        ArrayList<Ausencia> listaConVacio = new ArrayList<>();
        Ausencia aVacio = new Ausencia();
        aVacio.setNombreUsuario(getString(R.string.no_peticiones_ausencia));
        listaConVacio.add(aVacio);
        binding.listaAusencias.setAdapter(new AdaptadorAusenciasRecyclerView(listaConVacio));
    }

    @Override
    public void agregarAusenciasAadaptador(ArrayList<Ausencia> listaAusenciasCompleta) {
        binding.listaAusencias.setAdapter(new AdaptadorAusenciasRecyclerView(listaAusenciasCompleta));
    }

    @Override
    public void mostrarFalloLecturaBaseDeDatos() {
        Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
    }


}