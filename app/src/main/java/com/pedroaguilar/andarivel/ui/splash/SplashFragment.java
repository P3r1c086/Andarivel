package com.pedroaguilar.andarivel.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.pedroaguilar.andarivel.R;

/**
 * Fragmento inicial que muestra el logo de la app y tras 3 segundos realiza la accion de navegacion
 * (action_splash_to_login), para inflar el siguiente fragmento (LoginFragment)
 */
public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long time = 2000L;
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
            }
        };
        handler.postDelayed(r, time);
    }
}
