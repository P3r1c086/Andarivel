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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentAusenciasBinding;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class AusenciasFragment extends Fragment implements TabLayoutMediator.TabConfigurationStrategy {

    private FragmentAusenciasBinding binding;
    private ArrayList<String> titles;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAusenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database.getInfoUser(firebaseAuth.getUid(), new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    titles = new ArrayList<>();
                    Boolean esAdmin = (Boolean)((Map<String, Object>) task.getResult().getValue()).get("esAdiminstrador");
                    titles.add(getString(R.string.solicitar_ausencia_tab_title));
                    if (esAdmin!= null && esAdmin){
                        titles.add(getString(R.string.conceder_ausencia_tab_title));
                    } else {
                        titles.add(getString(R.string.notificar_ausencia_tab_title));
                    }
                    setViewPagerAdapter(esAdmin);
                    new TabLayoutMediator(binding.tabLayout, binding.viewPager2, AusenciasFragment.this).attach();
                }
            }
        });
    }

    public void setViewPagerAdapter(Boolean esAdmin){
        //LLAMAR SIEMPRE A REQUIEREACTIVITY Y REQUIERECONTEXT EN VEZ DE GETACTIVITY O GETCONTEXT

        AdaptadorAuseciasTabs adaptadorAuseciasTabs = new
                AdaptadorAuseciasTabs(requireActivity());
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SolicitarAusenciaFragment());
        if (esAdmin) {
            fragmentList.add(new ConcederAusenciaFragment());
        }
        else {
            fragmentList.add(new NotificarAusenciaFragment());
        }
        adaptadorAuseciasTabs.setData(fragmentList);
        binding.viewPager2.setAdapter(adaptadorAuseciasTabs);
    }


    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}



