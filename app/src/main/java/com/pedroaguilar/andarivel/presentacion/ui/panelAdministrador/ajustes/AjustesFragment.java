package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ajustes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentAjustesBinding;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

public class AjustesFragment extends Fragment implements AjustesView {

    private FragmentAjustesBinding binding;
    private final AjustesPresenter presenter = new AjustesPresenter();

//    final PanelAdministradorActivity pa = (PanelAdministradorActivity) getActivity();
//    SharedPreferences sp = pa.getSharedPreferences("SP", pa.MODE_PRIVATE);
//    final SharedPreferences.Editor editor = sp.edit();
//    int theme = sp.getInt("Theme", 1);


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
        setListeners();


    }

    private void setListeners() {
//        if(theme == 1){
//            binding.switchTema.setChecked(false);
//        }else{
//            binding.switchTema.setChecked(true);
//        }
//        binding.switchTema.setOnClickListener(v -> {
//
//            if (binding.switchTema.isChecked()){
//                editor.putInt("Theme", 0);
//            }else{
//                editor.putInt("Theme", 1);
//            }
//            editor.apply();
//            pa.setDayNight();
//        });
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