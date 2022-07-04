package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentPerfilBinding;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PerfilFragment extends Fragment implements PerfilView {

    private final PerfilPresenter presenter = new PerfilPresenter();

    private FragmentPerfilBinding binding;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10;

    private String nombre ="";
    private String apellidos ="";
    private String direccion ="";
    private String telefono ="";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
        presenter.getInfoUser();
    }
    private void setListeners(){
        binding.botonEditarPerfil.setOnClickListener(v -> {
            //Creamos un bundle para llevar los datos de este fragmento al de editar perfil
            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);
            bundle.putString("apellidos", apellidos);
            bundle.putString("direccion", direccion);
            bundle.putString("telefono", telefono);
            Navigation.findNavController(v).navigate(R.id.action_perfil_dest_to_editarPerfil_fragment, bundle);
        });
        binding.imgPerfil.setOnClickListener(v -> {
            if (checkAndRequestPermissions(getActivity())){
                chooseImage(getActivity());
            }
        });
        binding.botonBorrarPerfil.setOnClickListener(v -> {
            //Colocamos una ventana emergente para confirmar que se quiere borrar el usuario
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
            builder.setTitle(R.string.titulo_borrado);
            builder.setMessage(getString(R.string.aleta_borrado));
            builder.setPositiveButton(R.string.si, (dialog, which) -> {
                presenter.borrarFotoPerfil();
                presenter.borrarUsuario();
            })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
    // Metodo para manipular el resultado de los permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
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

    // Metodo para comprobar los permisos de escritura en el dispositivo y el acceso a la camara.
    public static boolean checkAndRequestPermissions(final Activity context) {
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
    public void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            if(optionsMenu[i].equals("Take Photo")){
                // Open the camera and get the photo
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
            else if(optionsMenu[i].equals("Choose from Gallery")){
                // choose from  external storage
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
            else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void guardarYSettearImagen(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        presenter.guardaImagenPerfil(data);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        presenter.dispose();
    }

    @Override
    public void setDatosUsuarioEnUi(String nombre, String apellidos, String direccion, String telefono, String mail, StorageReference url) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        //seteo los datos en los textView e imageView
        binding.tvNombreCompletoPerfilDato.setText(nombre.concat(" " + apellidos));
        binding.tvDireccionPerfilDato.setText(direccion);
        binding.tvTelefonoPerfilDato.setText(telefono);
        binding.tvEmailPerfilDato.setText(mail);
        setImagenUsuario(url);
    }

    @Override
    public void setImagenUsuario(StorageReference url) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
        //Determinamos el ancho del trazo
        circularProgressDrawable.setStrokeWidth(5f);
        //Longitud del radio
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();


        //para obtener la imagen usamos la libreria de Glide
        GlideApp.with(requireContext())
                //Cargar la URL del perfil pasandole el id del usuario
                .load(url)
                .placeholder(circularProgressDrawable)
                .circleCrop()
                //Si se produce algun error se carga la imagen por defecto de la app
                .error(R.mipmap.ic_launcher)
                //Le proporcionamos un id random a la cache de Glide para que al actualizarla luego vuelva a llamar a
                // la url se de cuenta de que es diferente y no la coja de su cache.
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(binding.imgPerfil);
    }

    @Override
    public void navegarAlLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        //Lanzamos la activity login
        startActivity(intent);
        //Finalizamos esta actividad
        requireActivity().finish();
    }

    @Override
    public void showErrorSubirImagen() {
        Toast.makeText(requireContext(), R.string.error_subir_imagen, Toast.LENGTH_SHORT).show();
    }
}