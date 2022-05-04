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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     * <p>
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: crear estados TRABAJANDO y DESCANSANDO
        binding.btFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setVisibility(View.INVISIBLE);
                binding.btFinalJornada.setVisibility(View.VISIBLE);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                binding.fechaEntrada.setText("Momento inicial " + format.format(Calendar.getInstance().getTime()));
                binding.fechaSalida.setText("");
                almacenarFechaYhoraInicial();
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos
            }
        });
        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setVisibility(View.VISIBLE);
                binding.btFinalJornada.setVisibility(View.INVISIBLE);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                binding.fechaSalida.setText("Momento final " + format.format(Calendar.getInstance().getTime().getTime()));
                almacenarFechaYhoraFinal();
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos

            }
        });
    }


    private void almacenarFechaYhoraInicial(){
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setHoraEntrada((String) binding.fechaEntrada.getText());
        databaseReference.child(Constantes.TABLA_HORARIOS).child(user.getID()).setValue(user);
    }
    private void almacenarFechaYhoraFinal(){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/horaSalida/", (String) binding.fechaSalida.getText());

        databaseReference.child(Constantes.TABLA_HORARIOS).child(mAuth.getCurrentUser().getUid()).updateChildren(childUpdates);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
