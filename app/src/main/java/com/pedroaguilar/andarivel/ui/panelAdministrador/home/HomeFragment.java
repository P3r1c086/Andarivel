package com.pedroaguilar.andarivel.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;

/**
 * Fragmento que contiene los botones de fichar y salir.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     *
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btFinalJornada.setEnabled(false);
        binding.btFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setEnabled(false);
                binding.btFinalJornada.setEnabled(true);
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos
            }
        });
        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setEnabled(true);
                binding.btFinalJornada.setEnabled(false);
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}