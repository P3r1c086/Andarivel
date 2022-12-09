package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.estadoTrabajadores;

import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;

public class EstadoTrabajadoresPresenter extends BasePresenter<EstadoTrabajadoresView> {

    //TODO: comprobar que usuarios han fichado en el dia en curso, para poder ver si falta alguien por fichar ese dia.
    // Contemplar dos estados, dia en curso(contempla las horas laborales) y viceversa.
    // Crear recyclerView para mostrar los fichajes.
    // Si hay fichajes dentro del periodo del dia en curso, mostrar los datos de los usuarios que han realizado ese fichaje.
    // Una vez pasadas las horas contempladas en el primer estado, se pasa al segundo estado y se eliminan esos datos del recyclerView.

    // LocalDateTime fecha1 = new LocalDateTime();
//    public void showDate1(){
//        try {
//
//            LocalDate hoy1 = LocalDate.now();
//            LocalTime ahora1 = LocalTime.now();
//            LocalDateTime fecha1 = LocalDateTime.of(hoy1, ahora1);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
//            Date date1 = dateFormat.parse("2019-09-16");
//            Date date2 = dateFormat.parse("2020-01-25");
//            Log.i("///////", "Date-1: " +
//                    dateFormat.format(date1));
////            System.out.println("Date-1: " +
////                    dateFormat.format(date1));
//            Log.i("///////", "Date-2: " +
//                    dateFormat.format(date2));
////            System.out.println("Date-2: " +
////                    dateFormat.format(date2));
//            if(date1.before(date2)){
////                System.out.println(
////                        "Date-1 is before Date-2");
//                Log.i("///////", "Date-1 is before Date-2");
//            }
//        } catch (ParseException ex) {
//        }
//
//        //Log.i("///////", fecha.toString());
//    }
}
