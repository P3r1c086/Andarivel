package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentPerfilBinding;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosFragment;

import java.io.ByteArrayOutputStream;
import java.util.UUID;


public class PerfilFragment extends CamaraYpermisosFragment implements PerfilView {

    private final PerfilPresenter presenter = new PerfilPresenter();
    private FragmentPerfilBinding binding;

    private String nombre = "";
    private String apellidos = "";
    private String direccion = "";
    private String telefono = "";


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
    public void guardarYSettearImagen(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        presenter.guardaImagenPerfil(data);
        presenter.guardarImagenPerfil(task -> presenter.setImagenUser());
    }
}