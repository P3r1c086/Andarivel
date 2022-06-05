package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
        //Accedemos a todas las ausencias.
        database.getAusencias(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    Map<String, Object> mapRaiz = (Map<String, Object>) snapshot.getValue();
                    if (mapRaiz != null  && !mapRaiz.isEmpty()) {
                        Boolean find = false;
                        for (Map.Entry<String, Object> entry : mapRaiz.entrySet()) {
                            Map<String, String> mapAusencia = (Map<String, String>) entry.getValue();
                            //Si el valor del dato usuario es igual al id del usuario al que notificar la ausencia,
                            //obtenemos los datos de esa ausencia y los mostramos.
                            if (mapAusencia.get("usuario").equals(FirebaseAuth.getInstance().getUid())) {
                                Ausencia ausencia = new Ausencia();
                                ausencia.setIdAusencia(mapAusencia.get("usuario"));
                                ausencia.setFechaInicioAusencia(mapAusencia.get("fechaInicio"));
                                ausencia.setFechaFinAusencia(mapAusencia.get("fechaFin"));
                                ausencia.setMotivoAusencia(mapAusencia.get("motivoAusencia"));
                                ausencia.setDescripcionAusencia(mapAusencia.get("descripcion"));
                                ausencia.setEstado(mapAusencia.get("estado"));

//                                   binding.tvNombreUsuarioAusenciaDato.setText(ausencia.getNombreUsuario());
                                showAusencia(ausencia);
                                find = true;
                            }
                        }
                        if (!find){
                            showNoHayAusencias();
                        }
                    } else {
                        showNoHayAusencias();
                    }
                } else {
                    showNoHayAusencias();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Metodo para esconder los datos innecesarios en el caso de que no haya ausencias que mostrar
     */
    private void showNoHayAusencias(){
        binding.tvDescripcionAusencia.setVisibility(View.GONE);
        binding.tvDescripcionAusenciaDato.setVisibility(View.GONE);
        binding.tvMotivoAusencia.setVisibility(View.GONE);
        binding.tvMotivoAusenciaDato.setVisibility(View.GONE);
        binding.tvFechaFinAusencia.setVisibility(View.GONE);
        binding.tvFechaguion.setVisibility(View.GONE);
        binding.tvFechaInicioAusencia.setVisibility(View.GONE);
        binding.tvEstado.setText(binding.tvEstado.getText() + "No hay ausencias Pendientes de aprobar");
    }
    /**
     * Metodo para visibilizar los datos en el caso de que haya ausencias que mostrar
     */
    private void showAusencia(Ausencia ausencia){
        binding.tvDescripcionAusencia.setVisibility(View.VISIBLE);
        binding.tvDescripcionAusenciaDato.setVisibility(View.VISIBLE);
        binding.tvMotivoAusencia.setVisibility(View.VISIBLE);
        binding.tvMotivoAusenciaDato.setVisibility(View.VISIBLE);
        binding.tvFechaFinAusencia.setVisibility(View.VISIBLE);
        binding.tvFechaguion.setVisibility(View.VISIBLE);
        binding.tvFechaInicioAusencia.setVisibility(View.VISIBLE);
        binding.tvMotivoAusenciaDato.setText(ausencia.getMotivoAusencia());
        binding.tvFechaInicioAusencia.setText(ausencia.getFechaInicioAusencia());
        binding.tvFechaFinAusencia.setText(ausencia.getFechaFinAusencia());
        binding.tvDescripcionAusenciaDato.setText(ausencia.getDescripcionAusencia());
        binding.tvEstado.setText(ausencia.getEstado());
    }
}
