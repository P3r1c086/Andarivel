package com.pedroaguilar.andarivel.ui.panelAdministrador.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.databinding.FragmentInformesBinding;
import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


public class InformesFragment extends Fragment  {
    private FragmentInformesBinding binding;
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public InformesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listaUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        leerTodosUsuariosDatabase();
    }

    public void leerTodosUsuariosDatabase(){

        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Fichaje> listaFichajes = new ArrayList<Fichaje>();
                    for (Map.Entry<String, Object> entry: ((Map<String, Object>)task.getResult().getValue()).entrySet()) {
                        Map<String, String> mapFichaje = (Map<String, String>) entry.getValue();
                        Fichaje fichaje = new Fichaje();
                        fichaje.setNombreUsuario(mapFichaje.get("nombre"));
                        fichaje.setFecha(mapFichaje.get("fecha"));
                        fichaje.setHoraEntrada(mapFichaje.get("horaEntrada"));
                        fichaje.setHoraSalida(mapFichaje.get("horaSalida"));
                        listaFichajes.add(fichaje);
                    }
                    database.getInfoUser(FirebaseAuth.getInstance().getUid(), new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Todo Recorrer como en lineas superiores y coger el nombre y setear en la lista
                                binding.listaUsuarios.setAdapter(new Adaptador(listaFichajes));
                            }
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "fallo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}