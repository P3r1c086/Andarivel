package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pedroaguilar.andarivel.databinding.ListaUsuariosFragmentBinding;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.CalendarioFragment;

import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios
 * Create by Pedro Aguilar Fernández on 29/12/2022 at 18:24
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class ListaUsuariosFragment extends Fragment implements ListaUsuariosView {

    private ListaUsuariosFragmentBinding binding;
    private final ListaUsuariosPresenter presenter = new ListaUsuariosPresenter();
    String titleSave = "";
    String descriptionSave = "";
    private ListaUsuariosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ListaUsuariosFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        binding.rvUsuariosEvento.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.leerEmailUsuarios();
        if (getArguments() != null) {
            titleSave = getArguments().getString("title");
            descriptionSave = getArguments().getString("description");
        }
        setListeners();
    }

    @Override
    public void agnadirListaEmail(ArrayList<String> list) {
        adapter = new ListaUsuariosAdapter(list);
        binding.rvUsuariosEvento.setAdapter(adapter);
    }

    private void setListeners() {
        binding.btnConfirmarUsuarios.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("bundleKey", adapter.getListaEmailsChecked().toString());
            result.putString("title", titleSave);
            result.putString("description", descriptionSave);
            getParentFragmentManager().setFragmentResult("requestKey", result);

            CalendarioFragment fr = new CalendarioFragment();
            fr.setArguments(result);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(ListaUsuariosFragment.this.getId(), fr);
            transaction.addToBackStack(null);
            // Commit a la transacción
            transaction.commit();
        });
    }
}
