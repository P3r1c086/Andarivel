package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados;

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
import com.pedroaguilar.andarivel.databinding.FragmentTablonAnunciosEmplBinding;
import com.pedroaguilar.andarivel.modelo.Anuncio;

import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados
 * Create by Pedro Aguilar Fernández on 15/12/2022 at 19:07
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class TablonAnunciosEmplFragment extends Fragment implements TablonAnunciosEmplView {

    private FragmentTablonAnunciosEmplBinding binding;
    private final TablonAnunciosEmplPresenter presenter = new TablonAnunciosEmplPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTablonAnunciosEmplBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        //El LayoutManager es responsable de medir y posicionar las vistas de elementos dentro de un RecyclerView
        //así como de determinar la política sobre cuándo reciclar las vistas de elementos que ya no son visibles para el usuario.
        binding.rvAnuncios.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.leerTodosAnunciosDatabase();
    }

    @Override
    public void agnadirListaAnunciosCompleta(ArrayList<Anuncio> list) {
        //Colocamos en el adaptador la lista de Anuncios que continen todos los datos necesarios.
        binding.rvAnuncios.setAdapter(new AnuncioAdapter(list));
    }

    @Override
    public void mostrarListaVacia() {
        ArrayList<Anuncio> listaConVacio = new ArrayList<>();
        Anuncio aVacio = new Anuncio();
        //En caso de que no haya fichajes todavia, notificamos al administrador que no hay fichajes.
        aVacio.setTitle(getString(R.string.no_hay_anuncios));
        listaConVacio.add(aVacio);
        binding.rvAnuncios.setAdapter(new AnuncioAdapter(listaConVacio));
    }

    @Override
    public void mostrarFalloFirebase() {
        Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
    }

}
