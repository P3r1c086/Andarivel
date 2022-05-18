package com.pedroaguilar.andarivel.ui.panelAdministrador.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroaguilar.andarivel.databinding.FragmentInformesBinding;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.ArrayList;


public class InformesFragment extends Fragment  {
    private FragmentInformesBinding binding;
    private RecyclerView listaTrabajadores;
    private ArrayList<Usuario> arrayListUsuarios = new ArrayList<Usuario>();

    public InformesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaTrabajadores = binding.listaUsuarios;
        listaTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
        Usuario us = new Usuario();
        us.setNombre("Pepito");
        us.setHoraEntrada("05/05/2022");
        us.setHoraSalida("Salimos");
        arrayListUsuarios.add(us);
        Adaptador adaptador = new Adaptador(arrayListUsuarios);
        listaTrabajadores.setAdapter(adaptador);
    }


}