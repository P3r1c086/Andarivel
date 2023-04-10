package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ajustes;

import static android.content.Context.MODE_PRIVATE;
import static com.pedroaguilar.andarivel.presentacion.comun.Constantes.NIGHT_MODE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentAjustesBinding;
import com.pedroaguilar.andarivel.presentacion.comun.Constantes;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

public class AjustesFragment extends Fragment implements AjustesView {

    private FragmentAjustesBinding binding;
    private final AjustesPresenter presenter = new AjustesPresenter();
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        preferences = requireActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES_ID, MODE_PRIVATE);
        setListeners();
    }

    private void setListeners() {
        binding.switchTema.setChecked(preferences.getBoolean(NIGHT_MODE, false));
        binding.switchTema.setOnCheckedChangeListener((compoundButton, checked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (checked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(NIGHT_MODE, true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(NIGHT_MODE, false);
            }
            editor.apply();
        });

        binding.tvBorrarPerfil.setOnClickListener(v -> {
            //Colocamos una ventana emergente para confirmar que se quiere borrar el usuario
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_delete);
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
        //Cerramos la sesion.
        binding.tvLogout.setOnClickListener(v -> {
            //Colocamos una ventana emergente para confirmar que se quiere borrar el usuario
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_logout);
            builder.setTitle(R.string.titulo_cerrar_sesion);
            builder.setMessage(getString(R.string.aleta_borrado));
            builder.setPositiveButton(R.string.si, (dialog, which) -> {
                        presenter.logout();
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public void navegarAlLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        //Lanzamos la activity login
        startActivity(intent);
        //Finalizamos esta actividad
        requireActivity().finish();
    }
}