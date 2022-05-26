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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                AdaptadorAuseciasTabs(requireActivity());// he sustituido this por getActivity()
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SolicitarAusenciaFragment());
        if (esAdmin) fragmentList.add(new ConcederAusenciaFragment());
        adaptadorAuseciasTabs.setData(fragmentList);
        binding.viewPager2.setAdapter(adaptadorAuseciasTabs);
    }


    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}

/*
    public AusenciasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAusenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Asociar un evento de clic al EditText
        binding.etFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaInicioDatePickerDialog();
            }
        });
        //Asociar un evento de clic al EditText
        binding.etFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFechaFinDatePickerDialog();
            }
        });
        setListeners();


    }*/


  /*  private void setListeners(){
        binding.spMotivoAusencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elementoSeleccionado = (String) parent.getItemAtPosition(position);
                usuario.setMotivoAusencia(elementoSeleccionado);
                //todo:falta pasarlo a la base de datos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        binding.btSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etDescripcion.getText().toString().isEmpty()){
                    usuario.setDescripcionAusencia("No especificado");
                }else {
                    usuario.setDescripcionAusencia(binding.etDescripcion.getText().toString());
                }
                //todo:falta pasarlo a la base de datos
                introducirDatosEnBd();
            }
        });
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
   /* private void showFechaInicioDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDateInicio = day + " / " + (month+1) + " / " + year;
                binding.etFechaInicio.setText(selectedDateInicio);
                usuario.setFechaInicioAusencia(selectedDateInicio);
                //almacenarFechaInicio();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Mostrar el DatePicker en un dialog
     */
   /* private void showFechaFinDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDateFin = day + " / " + (month+1) + " / " + year;
                binding.etFechaFin.setText(selectedDateFin);
                usuario.setFechaFinAusencia(selectedDateFin);
                //almacenarFechaFin();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void introducirDatosEnBd(){
        Usuario user = new Usuario();
        user.setID(mAuth.getCurrentUser().getUid());
        user.setMotivoAusencia(usuario.getMotivoAusencia());
        user.setFechaInicioAusencia(usuario.getFechaInicioAusencia());
        user.setFechaFinAusencia(usuario.getFechaFinAusencia());
        user.setDescripcionAusencia(usuario.getDescripcionAusencia());
        databaseReference.child(Constantes.TABLA_AUSENCIAS).child(user.getID()).setValue(user);
    }*/

