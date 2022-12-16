package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentTablonAnunciosAdminBinding;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosFragment;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador
 * Create by Pedro Aguilar Fernández on 14/12/2022 at 13:47
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class TablonAnunciosAdminFragment extends CamaraYpermisosFragment implements TablonAnunciosAdminView {

    private final TablonAnunciosAdminPresenter presenter = new TablonAnunciosAdminPresenter();
    private FragmentTablonAnunciosAdminBinding binding;
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTablonAnunciosAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
    }

    private void setListeners() {
        binding.btnSubir.setOnClickListener(v -> {
            Anuncio anuncio = new Anuncio();
            anuncio.setImgUrl(presenter.id + ".jpg");
            anuncio.setId(presenter.id);
            anuncio.setTitle(binding.etTitleAnuncio.getText().toString());
            anuncio.setDescripcion(binding.etDescriptionAnuncio.getText().toString());

            presenter.guardarDatosAnuncio(anuncio);
            Snackbar.make(binding.getRoot(), "Anuncio subido correctamente.", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.home_dest);
        });
        binding.imgAnuncio.setOnClickListener(v -> {
            if (checkAndRequestPermissions(getActivity())) {
                chooseImage(getActivity());
            }
        });
    }

    @Override
    public void guardarYSettearImagen(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        presenter.guardaImagenPerfil(data);
        presenter.guardarImagenAnuncio(task -> presenter.setImagenUser());
    }

    @Override
    public void setImagenUsuario(StorageReference url) {
        //para obtener la imagen usamos la libreria de Glide
        GlideApp.with(requireContext())
                //Cargar la URL del perfil pasandole el id del usuario
                .load(url)
                //Si se produce algun error se carga la imagen por defecto de la app
                .error(R.mipmap.ic_launcher)
                //Le proporcionamos un id random a la cache de Glide para que al actualizarla luego vuelva a llamar a
                // la url se de cuenta de que es diferente y no la coja de su cache.
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(binding.imgAnuncio);
    }
}
