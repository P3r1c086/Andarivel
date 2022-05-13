package com.pedroaguilar.andarivel.ui.panelAdministrador.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.databinding.FragmentEditarPerfilBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilFragment extends Fragment {

    private FragmentEditarPerfilBinding binding;
    private EditText nombre;
    private EditText apellidos;
    private EditText direccion;
    private EditText telefono;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    public EditarPerfilFragment() {
        // Required empty public constructor
    }



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
        binding.btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    actualizarUsuario();
                    Navigation.findNavController(v).navigateUp();
                }


            }

        });


    }

    private void actualizarUsuario(){
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/nombre/", (String) binding.etNombreReal.getText().toString());
            childUpdates.put("/apellidos/", (String) binding.etApellidos.getText().toString());
            childUpdates.put("/direccion/", (String) binding.etDireccion.getText().toString());
            childUpdates.put("/telefono/", (String) binding.etTelefono.getText().toString());
            databaseReference.child(Constantes.TABLA_USUARIOS).child(auth.getCurrentUser().getUid()).updateChildren(childUpdates);

    }
    private boolean validarCampos() {
        boolean resultado = true;
        String nombreReal = binding.etNombreReal.getText().toString();
        String apellido = binding.etApellidos.getText().toString();
        String direcion = binding.etDireccion.getText().toString();
        String telefon = binding.etTelefono.getText().toString();

        if (nombreReal.isEmpty()) {
            binding.etNombreReal.setError("Requerido");
            resultado = false;
        } else if (apellido.isEmpty()) {
            binding.etApellidos.setError("Requerido");
            resultado = false;
        } else if (direcion.isEmpty()) {
            binding.etDireccion.setError("Requerido");
            resultado = false;
        } else if (telefon.isEmpty()) {
            binding.etTelefono.setError("Requerido");
            resultado = false;
        }
        return resultado;
    }
}