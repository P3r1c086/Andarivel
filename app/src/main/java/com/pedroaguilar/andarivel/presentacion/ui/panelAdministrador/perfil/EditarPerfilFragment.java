package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentEditarPerfilBinding;

public class EditarPerfilFragment extends Fragment implements EditarPerfilView {

    private FragmentEditarPerfilBinding binding;
    private final EditarPerfilPresenter presenter = new EditarPerfilPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditarPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        //Traemos los datos de perfil que hay en bundle y los colocamos en los textInputLayout de este fragmento
        if (getArguments() != null) {
            binding.etNombreReal.setText(getArguments().getString("nombre"));
            binding.etApellidos.setText(getArguments().getString("apellidos"));
            binding.etDireccion.setText(getArguments().getString("direccion"));
            binding.etTelefono.setText(getArguments().getString("telefono"));
        }
        setListners();
    }

    private void setListners() {
        binding.btAceptar.setOnClickListener(v -> presenter.clickBotonAceptar(binding.etNombreReal.getText().toString(),
                binding.etApellidos.getText().toString(),
                binding.etDireccion.getText().toString(),
                binding.etTelefono.getText().toString()));
    }

    @Override
    public void showTelefonoNoValido() {
        Toast.makeText(getContext(), getString(R.string.telefono_no_valido), Toast.LENGTH_LONG).show();
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
    public void mostrarErrorRequeridoTelefono() {
        binding.etTelefono.setError(getString(R.string.requerido));
    }

    @Override
    public void showToastActualizadoCorrectamente() {
        Toast.makeText(getContext(), R.string.actualizado_correctamente, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navegarAperfil() {
        Navigation.findNavController(requireView()).navigateUp();
    }
}