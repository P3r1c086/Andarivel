package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

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

/**
 * Fragmento que contiene un Tablayout donde se inflaran los fragmentos necesarios para realizar las solicitudes, concesiones y
 * notificaciones de ausencias.
 */

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
        //se obtiene la informacion del usuario que esta logueado
        database.getInfoUser(firebaseAuth.getUid(), new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    titles = new ArrayList<>();
                    //Creamos una variable boolean para comprobar si el usuario es administrador o no
                    Boolean esAdmin = (Boolean)((Map<String, Object>) task.getResult().getValue()).get("esAdiminstrador");
                    //A todos los usuarios se le añadira la pestaña Solicitar
                    titles.add(getString(R.string.solicitar_ausencia_tab_title));
                    //Si es el administrador se le añade la pestaña de Conceder Ausencia. Si no es administrador se le añade la pestaña
                    //de Notificaciones.
                    if (esAdmin!= null && esAdmin){
                        titles.add(getString(R.string.conceder_ausencia_tab_title));
                    } else {
                        titles.add(getString(R.string.notificar_ausencia_tab_title));
                    }
                    setViewPagerAdapter(esAdmin);
                    //Se crea Un mediador para vincular un TabLayout con un ViewPager2. El mediador sincronizará la posición de ViewPager2
                    // con la pestaña seleccionada cuando se seleccione una pestaña, y la posición de desplazamiento de TabLayout cuando
                    // el usuario arrastre ViewPager2.
                    new TabLayoutMediator(binding.tabLayout, binding.viewPager2, AusenciasFragment.this).attach();
                }
            }
        });
    }

    public void setViewPagerAdapter(Boolean esAdmin){
        //LLAMAR SIEMPRE A REQUIEREACTIVITY Y REQUIERECONTEXT EN VEZ DE GETACTIVITY O GETCONTEXT
        //Se crea un objeto adaptador el cual contendra los fragmentos a mostrar.
        AdaptadorAuseciasTabs adaptadorAuseciasTabs = new
                AdaptadorAuseciasTabs(requireActivity());
        //Se crea un array donde colocar los fragmentos correspondientes al adaptador, dependiendo de si eres empleado o administrador.
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SolicitarAusenciaFragment());
        if (esAdmin) {
            fragmentList.add(new ConcederAusenciaFragment());
        }
        else {
            fragmentList.add(new NotificarAusenciaFragment());
        }
        adaptadorAuseciasTabs.setData(fragmentList);
        //Al ViewPager2 se le asigna un adaptador. El ViewPager es un administrador de layout que permite al usuario voltear hacia
        // la izquierda y hacia la derecha a través de páginas de datos.
        binding.viewPager2.setAdapter(adaptadorAuseciasTabs);
    }

    /**
     * Se llama para configurar la pestaña de la página en la posición especificada.
     */
    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}



