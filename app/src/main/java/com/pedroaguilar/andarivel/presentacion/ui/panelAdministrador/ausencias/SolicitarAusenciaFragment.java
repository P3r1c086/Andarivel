package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.material.snackbar.Snackbar;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentSolicitarAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosFragment;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class SolicitarAusenciaFragment extends CamaraYpermisosFragment implements SolicitarAusenciaView {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10;

    private FragmentSolicitarAusenciaBinding binding;
    private final SolicitarAusenciaPresenter presenter = new SolicitarAusenciaPresenter();
    private final Usuario usuario = new Usuario();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSolicitarAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
    }

    private void setListeners() {
        //Asociar un evento de clic al EditText para mostrar un dialog con un calendario y poder
        //seleccionar una fecha y tomar el dato.
        binding.etFechaInicio.setOnClickListener(v -> showFechaInicioDatePickerDialog());
        //Asociar un evento de clic al EditText para mostrar un dialog con un calendario y poder
        //seleccionar una fecha y tomar el dato.
        binding.etFechaFin.setOnClickListener(v -> showFechaFinDatePickerDialog());
        //Comprueba la opcion seleccionada en el spinner y obtiene el dato.
        binding.spMotivoAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elementoSeleccionado = (String) parent.getItemAtPosition(position);
                if (position != 0) {
                    usuario.setMotivoAusencia(elementoSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.btSolicitar.setOnClickListener(v -> {
            //Se validan los datos antes de generar una ausencia.
            presenter.botonSolicitarClickado(binding.etDescripcion.getText().toString(), usuario);
        });
        binding.btAdjuntarDoc.setOnClickListener(v -> {
            if (checkAndRequestPermissions(getActivity())) {
                chooseImage(getActivity());
            }
        });
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
    private void showFechaInicioDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
            // +1 porque enero es cero
            final String selectedDateInicio = day + " / " + (month + 1) + " / " + year;
            binding.etFechaInicio.setText(selectedDateInicio);
            usuario.setFechaInicioAusencia(selectedDateInicio);

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
    private void showFechaFinDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
            // +1 because January is zero
            final String selectedDateFin = day + " / " + (month + 1) + " / " + year;
            binding.etFechaFin.setText(selectedDateFin);
            usuario.setFechaFinAusencia(selectedDateFin);

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void showErrorMotivoAusencia() {
        Snackbar.make(binding.getRoot(), getString(R.string.toast_error_tipo_ausencia), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorFechaIncio() {
        Snackbar.make(binding.getRoot(), getString(R.string.toast_error_fecha_inicio_ausencia), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorFechaFin() {
        Snackbar.make(binding.getRoot(), getString(R.string.toast_error_fecha_fin_ausencia), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showAusenciaPendiente() {
        Snackbar.make(binding.getRoot(), R.string.ausencia_pendiente, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showAusenciaCreadaConExito() {
        Snackbar.make(binding.getRoot(), R.string.solicitar_ausencia_creacion_exito_mensaje, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void guardarYSettearImagen(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        presenter.guardaImagenPerfil(data);
        binding.tvEstadoDoc.setText(getString(R.string.imagen_lista_para_subir));
        binding.tvEstadoDoc.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorSubirImagen() {
        Snackbar.make(binding.getRoot(), R.string.error_subir_imagen, Snackbar.LENGTH_SHORT).show();
    }
}
