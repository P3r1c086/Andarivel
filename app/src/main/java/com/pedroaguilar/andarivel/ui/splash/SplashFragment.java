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
 * Fragmento inicial que muestra el logo de la app y tras 2 segundos realiza la accion de navegacion
 * (action_splash_to_login), para inflar el siguiente fragmento (LoginFragment)
 */
public class SplashFragment extends Fragment {

    /**
     * Aquí se crea la vista, inflando el layout fragment.splash
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return un objeto inlater el cual infla el fragmento
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Aquí se realiza la logica una vez inflada la vista en el metodo anterior
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Objeto Handler (manipulador), permite enviar y procesar objetos mensaje ejecutables. Esta asociado al objeto Runnable el cual iniciara el proceso,
         * en este caso lanza el proceso action_splash_to_login, el cual navega desde el splash hacia el login despues de un tiempo de 2 segundos.
         */
        long time = 2000L;//2000 milisegundos = 2 segundos
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                /**
                 * Este objeto Navigation es un singleton, es decir, solo se puede crear una instacia de el.
                 */
                Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
            }
        };
        /**
         * El método postDelayed, hace que Runnable r se agregue a la cola de mensajes, para que se ejecute después de que transcurra la cantidad de tiempo especificada.
         */
        handler.postDelayed(r, time);
    }
}
