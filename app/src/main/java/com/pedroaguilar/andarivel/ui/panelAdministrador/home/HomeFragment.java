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
import com.pedroaguilar.andarivel.utilidades.SortDescendingComparator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        //Al inflar la vista se pone la imagen del hombre con casco invisible.
        binding.imgEstado2.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Obtenemos todos los fichajes y colocamos la app en el estadoFinJornada o estadoInicioJornada dependiendo de si encuentra algun fichaje
        // completado o sin completar.
        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean estadoFinJornada = false;
                    //Creamos un mapa con todos los fichajes sin ordenar.
                    Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                    //Creamos otro mapa con todos los fichajes anteriores ordenados descendentemente, es decir, el primmero seria
                    // el ultimo  que se ha introducido.
                    TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                    //Añadimos entries del mapa devuelto por firebase
                    if (fichajes!= null ) sorted.putAll(fichajes);
                    //Recorremos el mapa ordenado buscando el usuario que coincida con el de la sesión y que tenga
                    // un nodo con horaSalida a null (que no exista el nodo)
                    for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                        if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))
                                && ((Map<String, Object>) entry.getValue()).get("horaSalida") == null){
                            estadoFinJornada = true;
                            break;
                        }
                    }
                    //En función de si lo hemos encontrado o no, setteamos en la pantalla un estado u otro
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
        childUpdates.clear();//todo: no entiendo para que hay que limpiar los hijos
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
                    //Mapa completamente desordenado, devuelto por firebase
                    Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                    //Construimos TreeMap con nuestro comparador de ordenacion descendente
                    TreeMap<String, Object> sorted = new TreeMap<>(new SortDescendingComparator());
                    //Añadimos entries del mapa devuelto por firebase
                    sorted.putAll(fichajes);
                    //Recorremos el mapa ordenado buscando valor que coincida con el usuario.
                    for (Map.Entry<String, Object> entry : sorted.entrySet()) {
                                            //key                          //value
                        if (Objects.equals(mAuth.getUid(), ((Map<String, Object>) entry.getValue()).get("usuario"))){
                            nodoFichaje = entry.getKey(); //me devuelve fichaje n
                            break;
                        }
                    }
                    //Actualizamos campo horaSalida del nodo encontrado.
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

