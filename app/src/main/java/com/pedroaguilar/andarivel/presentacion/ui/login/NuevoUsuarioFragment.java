package com.pedroaguilar.andarivel.presentacion.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentNuevoUsuarioBinding;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.PanelAdministradorActivity;

/**
 * Fragmento para que el usuario intrduzca la informacion para crear un nuevo usuario.
 * Al crear el nuevo usuario, si toddo esta bien, navegara a la actividad de PanelAdiminstrador
 */
public class NuevoUsuarioFragment extends Fragment implements NuevoUsuarioView {

    private final NuevoUsuarioPresenter presenter = new NuevoUsuarioPresenter();
    private FragmentNuevoUsuarioBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNuevoUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
    }

    private void setListeners() {
        binding.btAceptar.setOnClickListener(v -> {
            if (getActivity() != null) {
                presenter.botonAceptarClickado(binding.etNombreReal.getText().toString(),
                        binding.etApellidos.getText().toString(),
                        binding.etDireccion.getText().toString(),
                        binding.etEmail.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString(),
                        binding.etPass.getText().toString());
            }
        });
    }

    private void showError(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setErrorEmail() {
        binding.etEmail.setError(getString(R.string.debes_introducir_email));
    }

    @Override
    public void showErrorEmail() {
        showError(getString(R.string.debes_introducir_email_valido));
    }

    @Override
    public void showErrorDebesIntroducirContrasena() {
        showError(getString(R.string.debes_introducir_contrasena));
    }

    @Override
    public void showErrorContrasenaDebil() {
        showError(getString(R.string.contrasena_debil));
    }

    @Override
    public void showErrorTlfNoValido() {
        showError(getString(R.string.telefono_no_valido));
    }

    @Override
    public void mostrarErrorRequeridoNombre() {
        binding.etNombreReal.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoApellido() {
        binding.etApellidos.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoDireccion() {
        binding.etDireccion.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoCorreo() {
        binding.etEmail.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoTelefono() {
        binding.etTelefono.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoPass() {
        binding.etPass.setError(getString(R.string.requerido));
    }

    @Override
    public void toastEmailYaRegistrado() {
//        Toast.makeText(getContext(), R.string.email_ya_registrado, Toast.LENGTH_SHORT).show();
        Snackbar.make(binding.getRoot(), R.string.email_ya_registrado, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void falloAutenticacion() {
//        Toast.makeText(getContext(), R.string.autenticar_fallo, Toast.LENGTH_SHORT).show();
        Snackbar.make(binding.getRoot(), R.string.autenticar_fallo, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navegarActividadMenu() {
        //Navegamos a la nueva actividad y matamos esta para que no exista navegacion a ella de nuevo
        startActivity(new Intent(getContext(), PanelAdministradorActivity.class));
        getActivity().finish();
    }
}