package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentCalendarioBinding;

import java.util.Calendar;


public class CalendarioFragment extends Fragment {

    private FragmentCalendarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //eventoCalendario();
        mostrarFecha();
    }

    private void eventoCalendario() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2022, 11, 28, 11, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2022, 11, 28, 11, 31);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "pajaros33@gmail.com");
        startActivity(intent);
    }

    private void mostrarFecha() {
        binding.calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                binding.tvDate.setText(date);
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(year, month, dayOfMonth, 11, 30);
                //Calendar endTime = Calendar.getInstance();
                //endTime.set(2022, 11, 28, 11, 31);
                binding.btnEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                //.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                                .putExtra(Intent.EXTRA_EMAIL, "pajaros33@gmail.com");
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}