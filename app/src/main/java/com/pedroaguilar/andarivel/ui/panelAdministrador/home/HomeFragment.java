package com.pedroaguilar.andarivel.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Fragmento que contiene los botones de fichar y salir.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.imgEstado2.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean estadoFinJornada = false;
                    Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                    TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                    if (fichajes!= null ) sorted.putAll(fichajes);
                    for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                        if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))
                                && ((Map<String, Object>) entry.getValue()).get("horaSalida") == null){
                            estadoFinJornada = true;
                            break;
                        }
                    }
                    if (estadoFinJornada){
                        setEstadoFinalJornada();
                    } else {
                        setEstadoInicioJornada();
                    }

                }
            }
        });

        binding.btFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEstadoFinalJornada();
                almacenarFechaYhoraInicial();

            }
        });
        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEstadoInicioJornada();
                almacenarFechaYhoraFinal();
            }
        });
    }

    private void setEstadoInicioJornada(){
        binding.btFichar.setVisibility(View.VISIBLE);
        binding.btFinalJornada.setVisibility(View.INVISIBLE);
        binding.imgEstado2.setVisibility(View.INVISIBLE);
        binding.imgEstado1.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        binding.horaSalida.setText("" + format.format(Calendar.getInstance().getTime().getTime()));
        binding.fechaSalida.setText(dateformat.format(Calendar.getInstance().getTime().getTime()));
    }

    private void setEstadoFinalJornada(){
        binding.btFichar.setVisibility(View.INVISIBLE);
        binding.btFinalJornada.setVisibility(View.VISIBLE);
        binding.imgEstado1.setVisibility(View.INVISIBLE);
        binding.imgEstado2.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
        binding.horaEntrada.setText("" + format.format(Calendar.getInstance().getTime()));
        binding.fechaEntrada.setText(dateformat.format(Calendar.getInstance().getTime()));
        binding.horaSalida.setText("");
        binding.fechaSalida.setText("");
    }


    private void almacenarFechaYhoraInicial() {
        database.cuentaFichaje(new OnCompleteListener<DataSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getValue() == null){
                        crearNodoFichaje("1");
                    } else {
                        crearNodoFichaje((((Map<String, Object>)task.getResult().getValue()).entrySet().size() + 1) + "");
                    }
                } else {

                }
            }
        });

    }

    public void crearNodoFichaje(String nNodo){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Fichaje"+nNodo+"/fecha", binding.fechaEntrada.getText());
        childUpdates.put("/Fichaje"+nNodo+"/horaEntrada", binding.horaEntrada.getText());
        childUpdates.put("/Fichaje"+nNodo+"/usuario", mAuth.getUid());
        database.actualizarFichaje(childUpdates);
        childUpdates.clear();
        childUpdates.put("/Fichajes/Fichaje"+nNodo, true);
        database.actualizarDatosUsuario(mAuth.getUid(), childUpdates, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) { }
        });
    }

    private void almacenarFechaYhoraFinal() {
        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String nodoFichaje = "";
                    Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                    TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                    sorted.putAll(fichajes);
                    for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                        if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))){
                            nodoFichaje = entry.getKey();
                            break;
                        }
                    }
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/"+ nodoFichaje +"/horaSalida", (String) binding.horaSalida.getText());
                    database.actualizarFichaje(childUpdates);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

class SortDescendingComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        if (s1.length() == s2.length()){
            return (-1)* s1.compareTo(s2);
        } if (s1.length() > s2.length()){
            return -1;
        } else {
            return 1;
        }
    }
}
