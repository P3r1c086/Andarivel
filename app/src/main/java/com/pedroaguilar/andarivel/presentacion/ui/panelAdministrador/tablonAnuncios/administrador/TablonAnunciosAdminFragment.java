package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentTablonAnunciosAdminBinding;
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
        binding.etTitleAnuncio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    binding.btnSubir.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.btnAdd.setOnClickListener(v -> {
            binding.cbImagen.setEnabled(true);
            binding.cbLink.setEnabled(true);
            binding.cbLocation.setEnabled(true);
        });
        binding.cbImagen.setOnClickListener(v -> {
            if (binding.cbImagen.isChecked()) {
                binding.imgAnuncio.setVisibility(View.VISIBLE);
                binding.btnSubir.setVisibility(View.VISIBLE);
            } else {
                binding.imgAnuncio.setVisibility(View.INVISIBLE);
            }
        });
        binding.cbLink.setOnClickListener(v -> {
            if (binding.cbLink.isChecked()) {
                binding.tiLinkAnuncio.setVisibility(View.VISIBLE);
                binding.btnSubir.setVisibility(View.VISIBLE);
            } else {
                binding.tiLinkAnuncio.setVisibility(View.INVISIBLE);
            }
        });
        binding.cbLocation.setOnClickListener(v -> {
            if (binding.cbLocation.isChecked()) {
                binding.tvLocationAnuncio.setVisibility(View.VISIBLE);
                binding.btnSubir.setVisibility(View.VISIBLE);
            } else {
                binding.tvLocationAnuncio.setVisibility(View.INVISIBLE);
            }
        });
        binding.btnSubir.setOnClickListener(v -> {
//            Anuncio anuncio = new Anuncio();
//            anuncio.setImgUrl(presenter.id + ".jpg");
//            anuncio.setId(presenter.id);
//            anuncio.setTitle(binding.etTitleAnuncio.getText().toString());
//            anuncio.setDescripcion(binding.etDescriptionAnuncio.getText().toString());

            presenter.botonAceptarClickado(
                    binding.etTitleAnuncio.getText().toString(),
                    binding.etDescriptionAnuncio.getText().toString(), presenter.id,
                    binding.etLinkAnuncio.getText().toString());

//            presenter.botonAceptarClickado(anuncio.getTitle(), anuncio);
//            presenter.guardarDatosAnuncio(anuncio);
//            Snackbar.make(binding.getRoot(), "Anuncio subido correctamente.", Snackbar.LENGTH_SHORT).show();
//            Navigation.findNavController(v).navigate(R.id.home_dest);
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

    @Override
    public void mostrarErrorRequeridoTitle() {
        binding.etTitleAnuncio.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarExitoSubidaAnuncio() {
        Snackbar.make(binding.getRoot(), "Anuncio subido correctamente.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarError() {
        Snackbar.make(binding.getRoot(), "Se ha producido un error.", Snackbar.LENGTH_SHORT).show();
    }
}
