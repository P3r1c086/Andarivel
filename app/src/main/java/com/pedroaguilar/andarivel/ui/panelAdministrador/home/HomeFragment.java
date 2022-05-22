package com.pedroaguilar.andarivel.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Usuario;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.ui.panelAdministrador.perfil.PerfilFragment;

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
    private Calendar momentoPulsacion;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private PerfilFragment perfilFragment = new PerfilFragment();

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

    private void almacenarFechaYhoraInicial(){
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setHoraEntrada((String) binding.horaEntrada.getText());
        user.setFecha((String) binding.fechaEntrada.getText());
        //database.ficharEntrada(user.getID(),user);   // -----------------------  me crea un nodo con el nombre de la tabla dentro de la tabla-----------------
        //todo: en vez de tomar el id como hijo, tomar el numero de fichaje y agragar fecha, id, hora entrada y hora salida como hijos
        databaseReference.child(Constantes.TABLA_HORARIOS).child(user.getID()).setValue(user);
        Boolean fichaje = true;
        //todo: leer el num de fichajes que han hecho los usuarios para concatenar ese numero + 1 al hijo numFichajes
        databaseReference.child(Constantes.TABLA_USUARIOS).child(user.getID()).child("fichaje").child("numFichaje").setValue(fichaje);

    }
    private void almacenarFechaYhoraFinal(){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/horaSalida/", (String) binding.horaSalida.getText());
        childUpdates.put("/fechaSalida/", (String) binding.fechaSalida.getText());
        //database.ficharSalida(mAuth.getCurrentUser().getUid(),childUpdates);   // -----------------------  me crea un nodo con el nombre de la tabla dentro de la tabla-----------------
        databaseReference.child(Constantes.TABLA_HORARIOS).child(mAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
        //no hace falta actualizar
        //databaseReference.child(Constantes.TABLA_USUARIOS).child(mAuth.getCurrentUser().getUid()).child("fichaje").child("numFichaje").updateChildren(childUpdates);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
