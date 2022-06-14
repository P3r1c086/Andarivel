package com.pedroaguilar.andarivel.presentacion.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentLoginBinding;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.PanelAdministradorActivity;

/**
 * Fragmento segundo que contiene campos para que rellene el usuario y realice la autenticacion con
 * Firebase. Si es correcta iniciara la actividad de PanelAdiminstrador.
 * Contiene tambien boton para navegar al fragmento de crear nuevo usuario.
 */
public class LoginFragment extends Fragment {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor

    }


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

        TextView registrar = view.findViewById(R.id.tvIrRegistro);
        Button entrar = view.findViewById(R.id.btEntrar);

        TextInputEditText email = view.findViewById(R.id.etEmail);
        TextInputEditText password = view.findViewById(R.id.etPassword);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_login_to_register);
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), "Debes completar los campos", Toast.LENGTH_SHORT).show();
                    }else {
                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    //Firebase te envia la respuseta en un objeto tipo Task
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //Navegamos a la nueva actividad y matamos esta para que no exista navegacion a ella de nuevo. Aquí cambiamos de grafo de navegacion.
                                            startActivity(new Intent(getContext(), PanelAdministradorActivity.class));
                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });


    }

}