package com.pedroaguilar.andarivel.ui.panelAdministrador.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.BaseDatosFirebase;
import com.pedroaguilar.andarivel.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragmento que contiene los botones de fichar y salir.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Calendar momentoPulsacion;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     *
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
                binding.fechaFichado.setText("Momento inicial " + format.format(Calendar.getInstance().getTime()));
                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos
            }
        });
        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btFichar.setVisibility(View.VISIBLE);
                binding.btFinalJornada.setVisibility(View.INVISIBLE);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss EEEE dd 'de' MMMM 'de' YYYY", Locale.getDefault());
                binding.fechaFichado.setText(binding.fechaFichado.getText() +
                        " Momento final " + format.format(Calendar.getInstance().getTime().getTime()));

                //TODO: enviar datos del usuario y la hora a la que se ha pulsado a la base de datos

               // obtenerHoraFichado();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void obtenerDiaFichado(){
        BaseDatosFirebase bd = new BaseDatosFirebase();
        binding.btFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Calendar calendario = Calendar.getInstance();
                int y = calendario.get(Calendar.YEAR);
                int m = calendario.get(Calendar.MONTH);
                int d = calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String resultado = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
                        binding.diaFichado.setText(resultado);
                    }
                },y,m,d);
                datePickerDialog.show();*/
               // binding.diaFichado.setText(new Date().toString());
                //bd.leerEnBd();
            }
        });
    }
    private void obtenerHoraFichado(){

        binding.btFinalJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Calendar calendario = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendario.set(Calendar.MINUTE,minute);
                        SimpleDateFormat formatoTiempo = new SimpleDateFormat("HH:mm a");
                        String formatoFecha = formatoTiempo.format(calendario.getTime());
                        binding.horaFichado.setText(formatoFecha);
                    }
                },calendario.get(Calendar.HOUR_OF_DAY),calendario.get(Calendar.MINUTE),false);
                timePickerDialog.show();*/
                //binding.horaFichado.setText(new Date().toString());
            }
        });
    }

   /* public void fecha(){
          Time today = new Time(Time.getCurrentTimezone());
        //Optenemos la fecha actual */
         /* today.setToNow();
       // Creamos variables int y llamamos a los atributos de la clase time
        int dia = today.monthDay;
        int mes = today.month;
        int ano = today.year;
        /*El mes lo sumamos mas 1 por que la clase time me da un mes atrasado */
        /* mes = mes + 1;
        vt.setText("Fecha   Dia : " + dia + " Mes : "+mes+" Año : "+ano);
    }*/

/*
    Button btnDate;
    TextView vt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //para el icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //llamamo a la imagen por su nombre que tiene que estar en drawable
        getSupportActionBar().setIcon(R.drawable.ic_action_name);
        //instanciar la variable local con id  de los componentes del xml
        btnDate=(Button)findViewById(R.id.btnDate);
        vt=(TextView)findViewById(R.id.txtview);
        /*creamos un objeto de una clase y llamamo al metodo de la clase*/
     //   Time today=new Time(Time.getCurrentTimezone());
        /*Optenemos la fecha actual */
      //  today.setToNow();
        /*Creamos variables int y llamamos a los atributos de la clase time*/
      /*  int dia=today.monthDay;
        int mes=today.month;
        int ano=today.year;
        /*El mes lo sumamos mas 1 por que la clase time me da un mes atrasado */
      /*  mes=mes+1;
        vt.setText("Fecha   Dia : " + dia + " Mes : "+mes+" Año : "+ano);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDate.setText(new Date().toString());
            }
        });
    }*/
}
