package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentEstadoTrabajadoresBinding;

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

    }

}
