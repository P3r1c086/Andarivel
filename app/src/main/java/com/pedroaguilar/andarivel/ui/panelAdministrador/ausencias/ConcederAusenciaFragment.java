package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentConcederAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


public class ConcederAusenciaFragment extends Fragment {

    private FragmentConcederAusenciaBinding binding;
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public ConcederAusenciaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConcederAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listaAusencias.setLayoutManager(new LinearLayoutManager(getContext()));
        leerTodosUsuariosDatabase();
    }

    public void leerTodosUsuariosDatabase(){

        database.getAusencias(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Ausencia> listaAusencias = new ArrayList<Ausencia>();
                    Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                    if (mapRaiz != null  && !mapRaiz.isEmpty()) {
                        for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                            Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                            Ausencia ausencia = new Ausencia();
                            ausencia.setIdAusencia(entry.getKey());
                            ausencia.setNombreUsuario(mapAusencia.get("usuario"));
                            ausencia.setFechaInicioAusencia(mapAusencia.get("fechaInicio"));
                            ausencia.setFechaFinAusencia(mapAusencia.get("fechaFin"));
                            ausencia.setMotivoAusencia(mapAusencia.get("motivoAusencia"));
                            ausencia.setDescripcionAusencia(mapAusencia.get("descripcion"));
                            ausencia.setEstado(mapAusencia.get("estado"));
                            listaAusencias.add(ausencia);
                        }
                        database.getInfoUsers(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<Ausencia> listaAusenciasCompleta = new ArrayList<Ausencia>();
                                    Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                                    for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                                        Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                                        Ausencia a = findAusencia(listaAusencias, entry.getKey());
                                        while (a!= null) {
                                            listaAusencias.remove(a);
                                            a.setNombreUsuario(mapAusencia.get("nombre") + " " + mapAusencia.get("apellidos"));
                                            listaAusenciasCompleta.add(a);
                                            a = findAusencia(listaAusencias, entry.getKey());
                                        }

                                    }
                                    binding.listaAusencias.setAdapter(new AdaptadorAusenciasRecyclerView(listaAusenciasCompleta));
                                }
                            }
                        });
                    } else {
                        ArrayList<Ausencia> listaConVacio = new ArrayList<>();
                        Ausencia aVacio = new Ausencia();
                        aVacio.setNombreUsuario(getString(R.string.no_peticiones_ausencia));
                        listaConVacio.add(aVacio);
                        binding.listaAusencias.setAdapter(new AdaptadorAusenciasRecyclerView(listaConVacio));
                    }

                } else {
                    Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Ausencia findAusencia(ArrayList<Ausencia> list, String idUser){
        for (Ausencia a : list) {
            if (a.getNombreUsuario().equals(idUser)){
                return a;
            }
        }
        return null;
    }

}