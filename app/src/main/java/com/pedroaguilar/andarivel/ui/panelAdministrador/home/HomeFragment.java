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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

    //    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //TODO: crear estados TRABAJANDO y DESCANSANDO
//        binding.btFichar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.btFichar.setVisibility(View.INVISIBLE);
//                binding.btFinalJornada.setVisibility(View.VISIBLE);
//                //TODO: separar hora de fecha y crear dos textview diferentes donde colocarlas
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//                binding.fechaEntrada.setText("Momento inicial " + format.format(Calendar.getInstance().getTime()));
//                binding.fechaSalida.setText("");
//                almacenarFechaYhoraInicial();
//
//            }
//        });
//        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.btFichar.setVisibility(View.VISIBLE);
//                binding.btFinalJornada.setVisibility(View.INVISIBLE);
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
//                binding.fechaSalida.setText("Momento final " + format.format(Calendar.getInstance().getTime().getTime()));
//                almacenarFechaYhoraFinal();
//                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos
//
//            }
//        });
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setVisibility(View.INVISIBLE);
                binding.btFinalJornada.setVisibility(View.VISIBLE);
                binding.imgEstado1.setVisibility(View.INVISIBLE);
                binding.imgEstado2.setVisibility(View.VISIBLE);
                //TODO: separar hora de fecha y crear dos textview diferentes donde colocarlas
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                binding.horaEntrada.setText("" + format.format(Calendar.getInstance().getTime()));
                binding.fechaEntrada.setText(dateformat.format(Calendar.getInstance().getTime()));
                binding.horaSalida.setText("");
                binding.fechaSalida.setText("");
                almacenarFechaYhoraInicial();

            }
        });
        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setVisibility(View.VISIBLE);
                binding.btFinalJornada.setVisibility(View.INVISIBLE);
                binding.imgEstado2.setVisibility(View.INVISIBLE);
                binding.imgEstado1.setVisibility(View.VISIBLE);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat dateformat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                binding.horaSalida.setText("" + format.format(Calendar.getInstance().getTime().getTime()));
                binding.fechaSalida.setText(dateformat.format(Calendar.getInstance().getTime().getTime()));
                almacenarFechaYhoraFinal();
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos

            }
        });
    }

//    private void almacenarFechaYhoraInicial(){
//        Usuario user = new Usuario();
//        user.setID(mAuth.getCurrentUser().getUid());
//        user.setHoraEntrada((String) binding.fechaEntrada.getText());
//        //database.ficharEntrada(user.getID(),user);   // -----------------------  me crea un nodo con el nombre de la tabla dentro de la tabla-----------------
//        databaseReference.child(Constantes.TABLA_HORARIOS).child(user.getID()).setValue(user);
//        DatabaseReference horarioFichaje = FirebaseDatabase.getInstance().getReference().child(Constantes.TABLA_USUARIOS).child(user.getID()).child("HorarioFichaje");
//        horarioFichaje.setValue(user);
//    }
//    private void almacenarFechaYhoraFinal(){
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/horaSalida/", (String) binding.fechaSalida.getText());
//        //database.ficharSalida(mAuth.getCurrentUser().getUid(),childUpdates);   // -----------------------  me crea un nodo con el nombre de la tabla dentro de la tabla-----------------
//        databaseReference.child(Constantes.TABLA_HORARIOS).child(mAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
//        databaseReference.child(Constantes.TABLA_USUARIOS).child(mAuth.getCurrentUser().getUid()).child("HorarioFichaje").updateChildren(childUpdates);
//
//    }

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
                    //Controlar si firebase da error
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
        database.actualizarDatosUsuario(mAuth.getUid(), childUpdates);
    }

    private void almacenarFechaYhoraFinal() {
        database.getFichajes(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String nodoFichaje = "";
                    Map<String, Object> fichajes = (Map<String, Object>) task.getResult().getValue();
                    for (Map.Entry<String, Object> entry : fichajes.entrySet()) {
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
        //database.ficharSalida(mAuth.getCurrentUser().getUid(),childUpdates);   // -----------------------  me crea un nodo con el nombre de la tabla dentro de la tabla-----------------
        //databaseReference.child(Constantes.NODO_HORARIOS).child(mAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
        //no hace falta actualizar
        //databaseReference.child(Constantes.TABLA_USUARIOS).child(mAuth.getCurrentUser().getUid()).child("fichaje").child("numFichaje").updateChildren(childUpdates);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
