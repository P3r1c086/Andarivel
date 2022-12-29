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

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentCalendarioBinding;

import java.util.Calendar;


public class CalendarioFragment extends Fragment implements CalendarioView {

    private FragmentCalendarioBinding binding;
    private final CalendarioPresenter presenter = new CalendarioPresenter();
    private Calendar date = null;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        //eventoCalendario();
        //mostrarFecha();
        setListeners();
    }

    private void setListeners() {
        binding.calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(year, month, dayOfMonth, 11, 30);
            date = beginTime;
        });
        binding.btnEvent.setOnClickListener(v -> {
            if (getActivity() != null) {
                presenter.botonCrearEvento(binding.etTitleEven.getText().toString(),
                        binding.etDescriptionEvent.getText().toString(), date);
            }
        });
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
    //    private void eventoCalendario() {
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2022, 11, 28, 11, 30);
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2022, 11, 28, 11, 31);
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
//                .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                .putExtra(Intent.EXTRA_EMAIL, "pajaros33@gmail.com");
//        startActivity(intent);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}