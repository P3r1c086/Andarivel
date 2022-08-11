package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentSolicitarAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SolicitarAusenciaFragment extends Fragment implements SolicitarAusenciaView {

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
        Toast.makeText(getContext(), getString(R.string.toast_error_tipo_ausencia), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorFechaIncio() {
        Toast.makeText(getContext(), getString(R.string.toast_error_fecha_inicio_ausencia), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorFechaFin() {
        Toast.makeText(getContext(), getString(R.string.toast_error_fecha_fin_ausencia), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAusenciaPendiente() {
        Toast.makeText(requireContext(), R.string.ausencia_pendiente, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAusenciaCreadaConExito() {
        Toast.makeText(requireContext(), R.string.solicitar_ausencia_creacion_exito_mensaje, Toast.LENGTH_LONG).show();
    }

    // Metodo para comprobar los permisos de escritura en el dispositivo y el acceso a la camara.
    private boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Función que permite al usuario elegir una imagen de la cámara o de la galería.
    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            if (optionsMenu[i].equals("Take Photo")) {
                // Open the camera and get the photo
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (optionsMenu[i].equals("Choose from Gallery")) {
                // choose from  external storage
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    // Metodo para manipular el resultado de los permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(requireContext());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        guardarYSettearImagen(selectedImage);

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                guardarYSettearImagen(bitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

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
        Toast.makeText(requireContext(), R.string.error_subir_imagen, Toast.LENGTH_SHORT).show();
    }
}
