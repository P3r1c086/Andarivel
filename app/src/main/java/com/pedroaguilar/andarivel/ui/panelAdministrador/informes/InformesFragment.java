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
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.R;
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
        //El LayoutManager es responsable de medir y posicionar las vistas de elementos dentro de un RecyclerView
        //así como de determinar la política sobre cuándo reciclar las vistas de elementos que ya no son visibles para el usuario.
        binding.listaUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        leerTodosUsuariosDatabase();
    }

    public void leerTodosUsuariosDatabase(){
        //Obtenemos todos los fichajes.
        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Fichaje> listaFichajes = new ArrayList<Fichaje>();
                    //En este mapa obtenemos un mapa con todos los fichajes.
                    Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                    if (mapRaiz != null  && !mapRaiz.isEmpty()) {
                        for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                            //En este mapa obtenemos los datos de cada fichaje.
                            Map<String, String> mapFichaje = (Map<String, String>) entry.getValue();
                            Fichaje fichaje = new Fichaje();
                            fichaje.setIDUsuario(mapFichaje.get("usuario"));
                            fichaje.setFecha(mapFichaje.get("fecha"));
                            fichaje.setHoraEntrada(mapFichaje.get("horaEntrada"));
                            fichaje.setHoraSalida(mapFichaje.get("horaSalida"));
                            listaFichajes.add(fichaje);
                        }
                        database.getInfoUsers(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<Fichaje> listaFichajesCompleta = new ArrayList<Fichaje>();
                                    //Obtenemos un mapa con todos los usuarios.
                                    Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                                    for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                                        //Obtenemos un mapa con los datos de los fichajes.
                                        Map<String, String> mapFichaje = (Map<String, String>) entry.getValue();
                                        //Nos devuelve el fichaje que coincide con el id del usuario.
                                        Fichaje f = findFichaje(listaFichajes, entry.getKey());
                                        //Mientras exista el fichaje.
                                        while (f!= null) {
                                            listaFichajes.remove(f);//todo; no entiendo por que hay que borrar el fichaje de la lista
                                            //Le concatenamos el valor de nombre y apelldos del usuario y se lo asignamos al fichaje.
                                            f.setNombreUsuario(mapFichaje.get("nombre") + " " + mapFichaje.get("apellidos"));
                                            //Añadimos el fichaje con todos los datos necesarios a la listaFichajesCompleta
                                            listaFichajesCompleta.add(f);
                                            //todo:En f asignamos el fichaje que coincide con el id del usuario?
                                            f = findFichaje(listaFichajes, entry.getKey());
                                        }

                                    }
                                    //Colocamos en el adaptador la lista de fichajes que continen todos los datos necesarios.
                                    binding.listaUsuarios.setAdapter(new Adaptador(listaFichajesCompleta));
                                }
                            }
                        });
                    } else {
                        ArrayList<Fichaje> listaConVacio = new ArrayList<>();
                        Fichaje fVacio = new Fichaje();
                        //En caso de que no haya fichajes todavia, notificamos al administrador que no hay fichajes.
                        fVacio.setNombreUsuario(getString(R.string.no_hay_fichajes));
                        listaConVacio.add(fVacio);
                        binding.listaUsuarios.setAdapter(new Adaptador(listaConVacio));
                    }

                } else {
                    Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Encuentra el fichaje que coincida con el id del usuaario
     * @param list de objetos Fichaje
     * @param idUser identificador del usuario
     * @return el objeto Fichaje
     */
    private Fichaje findFichaje(ArrayList<Fichaje> list, String idUser){
        for (Fichaje f : list) {
            if (f.getIDUsuario().equals(idUser)){
                return f;
            }
        }
        return null;
    }
}