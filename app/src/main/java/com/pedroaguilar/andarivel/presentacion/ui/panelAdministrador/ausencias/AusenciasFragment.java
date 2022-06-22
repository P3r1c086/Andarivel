package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentAusenciasBinding;

import java.util.ArrayList;

/**
 * Fragmento que contiene un Tablayout donde se inflaran los fragmentos necesarios para realizar las solicitudes, concesiones y
 * notificaciones de ausencias.
 */

public class AusenciasFragment extends Fragment implements TabLayoutMediator.TabConfigurationStrategy, AusenciasView {

    private FragmentAusenciasBinding binding;
    private final AusenciasPresenter presenter = new AusenciasPresenter();
    private ArrayList<String> titles;

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
        presenter.initialize(this);
        //se obtiene la informacion del usuario que esta logueado
        presenter.infoUser();
    }

    /**
     * Se llama para configurar la pestaña de la página en la posición especificada.
     */
    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }

    @Override
    public void setInfoAdmin(Boolean esAdmin) {
        titles = new ArrayList<>();
        //A todos los usuarios se le añadira la pestaña Solicitar
        titles.add(getString(R.string.solicitar_ausencia_tab_title));
        //Si es el administrador se le añade la pestaña de Conceder Ausencia. Si no es administrador se le añade la pestaña
        //de Notificaciones.
        if (esAdmin != null && esAdmin) {
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

    private void setViewPagerAdapter(Boolean esAdmin) {
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

}



