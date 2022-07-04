package com.pedroaguilar.andarivel.presentacion.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentLoginBinding;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.PanelAdministradorActivity;

/**
 * Fragmento segundo que contiene campos para que rellene el usuario y realice la autenticacion con
 * Firebase. Si es correcta iniciara la actividad de PanelAdiminstrador.
 * Contiene tambien boton para navegar al fragmento de crear nuevo usuario.
 */
public class LoginFragment extends Fragment implements LoginView {

    private FragmentLoginBinding binding;
    private final LoginPresenter presenter = new LoginPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
    }

    public void setListeners() {
        binding.tvIrRegistro.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_login_to_register));
        binding.btEntrar.setOnClickListener(v -> {
            if (getActivity() != null) {
                if (binding.etEmail.getText().toString().isEmpty() || binding.etPassword.getText().toString().isEmpty()) {
                    showErrorCompletarCampos();
                } else {
                    presenter.signInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                }
            }
        });
    }

    private void showErrorCompletarCampos() {
        Toast.makeText(getContext(), R.string.debes_completar_campos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorAutenticarFallo() {
        Toast.makeText(getContext(), R.string.autenticar_fallo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navegarActividadMenu() {
        //Navegamos a la nueva actividad y matamos esta para que no exista navegacion a ella de nuevo. Aquí cambiamos de grafo de navegacion.
        startActivity(new Intent(getContext(), PanelAdministradorActivity.class));
        getActivity().finish();
    }
}