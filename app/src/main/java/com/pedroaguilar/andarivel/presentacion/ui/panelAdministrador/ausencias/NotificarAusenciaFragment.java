package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentNotificacionAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Ausencia;

import java.io.File;
import java.util.Objects;

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
        if (Objects.equals(ausencia.getEstado(), "Pendiente")) {
            binding.btnBorrarAusencia.setVisibility(View.VISIBLE);
            binding.btnBorrarAusencia.setOnClickListener(v -> {
                presenter.deleteAusencia(ausencia);
            });

        }
//        else{
//            binding.btnEdit.setVisibility(View.INVISIBLE);
//            binding.btnBorrarAusencia.setVisibility(View.INVISIBLE);
//            binding.imgAdjuntarDoc.setVisibility(View.INVISIBLE);
//        }
        if (ausencia.getAdjunto() != null) {
            binding.imgAdjuntarDoc.setVisibility(View.VISIBLE);
            binding.imgAdjuntarDoc.setOnClickListener(view -> {
                presenter.onClickBotonAdjunto(createTempFile(), task -> {
                    if (task.isSuccessful()) {
                        viewDoc(presenter.localDoc);
                    } else {
                        Toast.makeText(getContext(), "Error al descargar el adjunto", Toast.LENGTH_LONG).show();
                    }
                });
            });
        } else {
            binding.imgAdjuntarDoc.setVisibility(View.GONE);
        }
    }

    private File createTempFile(){
        File file = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(file.getAbsolutePath() + "/ImagenesDeAndarivel");
        dir.mkdirs();
        return new File(dir, presenter.ausenciaMostrada.getAdjunto());
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
        binding.btnBorrarAusencia.setVisibility(View.INVISIBLE);
        binding.imgAdjuntarDoc.setVisibility(View.INVISIBLE);
        binding.tvEstado.setText("No hay ausencias Pendientes de aprobar");
    }

    private void viewDoc(File file) {
        Uri photoURI = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
