package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentTablonAnunciosBinding;

public class TablonAnunciosFragment extends Fragment implements TablonAnunciosView {

    private FragmentTablonAnunciosBinding binding;
    private final TablonAnunciosPresenter presenter = new TablonAnunciosPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTablonAnunciosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);

    }

}
