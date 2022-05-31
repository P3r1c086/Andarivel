package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

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
import com.pedroaguilar.andarivel.databinding.FragmentNotificacionAusenciaBinding;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.Map;

public class NotificarAusenciaFragment extends Fragment {

    private FragmentNotificacionAusenciaBinding binding;
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    public NotificarAusenciaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificacionAusenciaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       database.getAusencias(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {
               if (task.isSuccessful()){
                   if (task.getResult().getValue() != null){
                       Map<String, Object> mapRaiz = (Map<String, Object>) task.getResult().getValue();
                       if (mapRaiz != null  && !mapRaiz.isEmpty()) {
                           Boolean find = false;
                           for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                               Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                               if (mapAusencia.get("usuario").equals(FirebaseAuth.getInstance().getUid())) {
                                   Ausencia ausencia = new Ausencia();
                                   ausencia.setIdAusencia(mapAusencia.get("usuario"));
                                   ausencia.setFechaInicioAusencia(mapAusencia.get("fechaInicio"));
                                   ausencia.setFechaFinAusencia(mapAusencia.get("fechaFin"));
                                   ausencia.setMotivoAusencia(mapAusencia.get("motivoAusencia"));
                                   ausencia.setDescripcionAusencia(mapAusencia.get("descripcion"));
                                   ausencia.setEstado(mapAusencia.get("estado"));

                                   binding.tvNombreUsuarioAusenciaDato.setText(ausencia.getNombreUsuario());
                                   binding.tvMotivoAusenciaDato.setText(ausencia.getMotivoAusencia());
                                   binding.tvFechaInicioAusencia.setText(ausencia.getFechaInicioAusencia());
                                   binding.tvFechaFinAusencia.setText(ausencia.getFechaFinAusencia());
                                   binding.tvDescripcionAusenciaDato.setText(ausencia.getDescripcionAusencia());
                                   binding.tvEstado.setText(ausencia.getEstado());
                                   find = true;
                               }
                           }
                           if (!find){
                               binding.tvEstado.setText(binding.tvEstado.getText() + "No hay ausencias Pendientes de aprobar");
                           }
                       } else {
                           binding.tvEstado.setText(binding.tvEstado.getText() + "No hay ausencias Pendientes de aprobar");
                       }
                   } else {
                       binding.tvEstado.setText(binding.tvEstado.getText() + "No hay ausencias Pendientes de aprobar");
                   }
               }
           }
       });
    }
}
