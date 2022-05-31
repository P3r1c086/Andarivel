package com.pedroaguilar.andarivel.ui.panelAdministrador.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.databinding.FragmentEditarPerfilBinding;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditarPerfilFragment extends Fragment {

    private FragmentEditarPerfilBinding binding;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
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
        if (getArguments()!= null){
            binding.etNombreReal.setText(getArguments().getString("nombre"));
            binding.etApellidos.setText(getArguments().getString("apellidos"));
            binding.etDireccion.setText(getArguments().getString("direccion"));
            binding.etTelefono.setText(getArguments().getString("telefono"));
        }

        binding.btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    actualizarUsuario();
                    Navigation.findNavController(v).navigateUp();
                    Toast.makeText(getContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
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
        database.actualizarDatosUsuario(auth.getCurrentUser().getUid() , childUpdates , new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) { }
        });
    }
    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
    private boolean validarTelefono() {
        boolean resultado = true;
        //Recuperamos el contenido del textInputLayout
        String tlf = binding.etTelefono.getText().toString();
        // Patrón con expresiones regulares
        Pattern tlfRegex = Pattern.compile(
                "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                        + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"
                        + ".{9,13}$"//longitud entre 9 y 13 caracteres
        );
        if (!tlfRegex.matcher(tlf).matches()) {
            showError("El teléfono no es válido");
            resultado = false;
        }
        return resultado;
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
        if (!telefon.isEmpty()) {
            resultado = validarTelefono();

        }
        return resultado;
    }
}