package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentCalendarioBinding;
import com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios.ListaUsuariosFragment;

import java.util.Calendar;


public class CalendarioFragment extends Fragment implements CalendarioView {

    private FragmentCalendarioBinding binding;
    private final CalendarioPresenter presenter = new CalendarioPresenter();
    private Calendar date = null;
    private String result;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        setListeners();
        if (getArguments() != null) {
            binding.etTitleEven.setText(getArguments().getString("title"));
            binding.etDescriptionEvent.setText(getArguments().getString("description"));
        }
    }

    private void setListeners() {
        binding.calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(year, month, dayOfMonth, 11, 30);
            date = beginTime;
        });
        binding.btnAddUsers.setOnClickListener(v -> {
            ListaUsuariosFragment fr = new ListaUsuariosFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", binding.etTitleEven.getText().toString());
            bundle.putString("description", binding.etDescriptionEvent.getText().toString());
            Navigation.findNavController(v).navigate(R.id.action_calendario_dest_to_listaUsuariosFragment, bundle);
        });
        binding.btnEvent.setOnClickListener(v -> {
            if (getActivity() != null) {
                presenter.botonCrearEvento(binding.etTitleEven.getText().toString(),
                        binding.etDescriptionEvent.getText().toString(), date, obtenerDatosListaUsuariosFragment());
            }
        });
    }

    private String obtenerDatosListaUsuariosFragment() {
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                result = bundle.getString("bundleKey");
                // Do something with the result...
            }
        });
        return result;
    }

    @Override
    public void lanzarCalendar(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void mostrarErrorRequeridoTitle() {
        binding.etTitleEven.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarErrorRequeridoDescription() {
        binding.etDescriptionEvent.setError(getString(R.string.requerido));
    }

    @Override
    public void mostrarToastDateRequerido() {
        Toast.makeText(getContext(), R.string.fecha_requerida, Toast.LENGTH_SHORT).show();
    }
}