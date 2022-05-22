package com.pedroaguilar.andarivel.ui.panelAdministrador.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Fichaje;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
                binding.horaEntrada.setText("Inicio jornada " + format.format(Calendar.getInstance().getTime()));
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
                binding.horaSalida.setText("Final jornada " + format.format(Calendar.getInstance().getTime().getTime()));
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
                        crearNodoFichaje(task.getResult().getValue().toString());
                    }
                } else {

                }
            }
        });

    }

    public void crearNodoFichaje(String nNodo){
        Fichaje fichaje = new Fichaje();
        fichaje.setUsuario(mAuth.getUid());
        fichaje.setFecha((String) binding.fechaEntrada.getText());
        fichaje.setHoraEntrada((String) binding.horaEntrada.getText());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Fichaje"+nNodo+"/fecha", fichaje.getFecha());
        childUpdates.put("/Fichaje"+nNodo+"/horaEntrada", fichaje.getHoraEntrada());
        childUpdates.put("/Fichaje"+nNodo+"/usuario", fichaje.getUsuario());
        database.abrirFichaje(childUpdates);
    }

    private void almacenarFechaYhoraFinal() {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/horaSalida/", (String) binding.horaSalida.getText());
        childUpdates.put("/fechaSalida/", (String) binding.fechaSalida.getText());
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
