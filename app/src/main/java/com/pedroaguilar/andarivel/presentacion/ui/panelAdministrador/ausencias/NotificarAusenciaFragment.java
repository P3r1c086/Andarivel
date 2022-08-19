package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentNotificacionAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Ausencia;

public class NotificarAusenciaFragment extends Fragment implements NotificarAusenciaView {

    private FragmentNotificacionAusenciaBinding binding;
    private final NotificarAusenciaPresenter presenter = new NotificarAusenciaPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificacionAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        presenter.obtenerAusencias();
    }

    @Override
    /**
     * Metodo para visibilizar los datos en el caso de que haya ausencias que mostrar
     */
    public void showAusencia(Ausencia ausencia) {
        binding.tvDescripcionAusencia.setVisibility(View.VISIBLE);
        binding.tvDescripcionAusenciaDato.setVisibility(View.VISIBLE);
        binding.tvMotivoAusencia.setVisibility(View.VISIBLE);
        binding.tvMotivoAusenciaDato.setVisibility(View.VISIBLE);
        binding.tvFechaFinAusencia.setVisibility(View.VISIBLE);
        binding.tvFechaguion.setVisibility(View.VISIBLE);
        binding.tvFechaInicioAusencia.setVisibility(View.VISIBLE);
        binding.tvMotivoAusenciaDato.setText(ausencia.getMotivoAusencia());
        binding.tvFechaInicioAusencia.setText(ausencia.getFechaInicioAusencia());
        binding.tvFechaFinAusencia.setText(ausencia.getFechaFinAusencia());
        binding.tvDescripcionAusenciaDato.setText(ausencia.getDescripcionAusencia());
        binding.tvEstado.setText(ausencia.getEstado());
        if (ausencia.getAdjunto() != null) {
            binding.imgAdjuntarDoc.setVisibility(View.VISIBLE);
        } else {
            binding.imgAdjuntarDoc.setVisibility(View.GONE);
        }
    }

    @Override
    /**
     * Metodo para esconder los datos innecesarios en el caso de que no haya ausencias que mostrar
     */
    public void showNoHayAusencias() {
        binding.tvDescripcionAusencia.setVisibility(View.GONE);
        binding.tvDescripcionAusenciaDato.setVisibility(View.GONE);
        binding.tvMotivoAusencia.setVisibility(View.GONE);
        binding.tvMotivoAusenciaDato.setVisibility(View.GONE);
        binding.tvFechaFinAusencia.setVisibility(View.GONE);
        binding.tvFechaguion.setVisibility(View.GONE);
        binding.tvFechaInicioAusencia.setVisibility(View.GONE);
        binding.tvEstado.setText(binding.tvEstado.getText() + "No hay ausencias Pendientes de aprobar");
    }

}
