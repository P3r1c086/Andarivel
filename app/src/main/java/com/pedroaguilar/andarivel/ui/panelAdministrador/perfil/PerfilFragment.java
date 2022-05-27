package com.pedroaguilar.andarivel.ui.panelAdministrador.perfil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.UploadTask;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentPerfilBinding;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;
import com.pedroaguilar.andarivel.ui.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PerfilFragment extends Fragment {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10;
    private FragmentPerfilBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    private String nombre ="";
    private String apellidos ="";
    private String direccion ="";
    private String telefono ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);*/

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
        //obtengo el hijo de usuarios con este id
        //si quiero solo una foto o momento, es decir, que no me llegue tod el rato llamo a get()
        database.getInfoUser(firebaseAuth.getUid(), new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    //obtengo los datos de firebase
                    //String uid = "" + snapshot.child("id").getValue(); si quiero sacar el id
                    nombre = "" + task.getResult().child("nombre").getValue();
                    apellidos = "" + task.getResult().child("apellidos").getValue();
                    direccion = "" + task.getResult().child("direccion").getValue();
                    telefono = "" + task.getResult().child("telefono").getValue();
                    String email = "" + task.getResult().child("email").getValue();
                    // String imagenPerfil = "" + snapshot.child("imagen").getValue();//en el caso de meter la imagen en la base de datos

                    //seteo los datos en los textView e imageView
                    binding.tvNombreCompletoPerfilDato.setText(nombre.concat(" " + apellidos));
                    binding.tvDireccionPerfilDato.setText(direccion);
                    binding.tvTelefonoPerfilDato.setText(telefono);
                    binding.tvEmailPerfilDato.setText(email);

                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();


                    //para obtener la imagen usamos la libreria de Glide
                    GlideApp.with(requireContext())
                            .load(storage.getUserPerfilUrl(firebaseAuth.getUid()))
                            .placeholder(circularProgressDrawable)
                            .circleCrop()
                            .error(R.mipmap.ic_launcher)
                            .signature(new ObjectKey(UUID.randomUUID().toString()))
                            .into(binding.imgPerfil);

                }
            }
        });
        binding.botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nombre", nombre);
                bundle.putString("apellidos", apellidos);
                bundle.putString("direccion", direccion);
                bundle.putString("telefono", telefono);
                Navigation.findNavController(v).navigate(R.id.action_perfil_dest_to_editarPerfil_fragment, bundle);
            }
        });
        binding.imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(getActivity())){
                    chooseImage(getActivity());
                }
            }
        });
        binding.botonBorrarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Mostrar AlertDialog para en la confirmacion llamar a:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
                builder.setTitle(R.string.titulo_borrado);
                builder.setMessage(getString(R.string.aleta_borrado));
                        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storage.borrarFotoPerfil(firebaseAuth.getUid());
                                database.borrarUsuario(firebaseAuth.getUid(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseAuth.getCurrentUser().delete();
                                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        requireActivity().finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // function to check permission
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

    // Handled permission Result
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

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
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

    private void guardarYSettearImagen(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storage.guardarImagenDePerfil(FirebaseAuth.getInstance().getUid(), data, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                //Fail to upload image
            }
        }, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(requireContext());
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();


                GlideApp.with(requireContext())
                        .load(storage.getUserPerfilUrl(firebaseAuth.getUid()))
                        .placeholder(circularProgressDrawable)
                        .circleCrop()
                        .error(R.mipmap.ic_launcher)
                        .signature(new ObjectKey(UUID.randomUUID().toString()))
                        .into(binding.imgPerfil);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}