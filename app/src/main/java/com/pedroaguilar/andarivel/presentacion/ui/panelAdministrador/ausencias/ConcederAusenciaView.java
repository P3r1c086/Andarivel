package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

import java.util.ArrayList;

public interface ConcederAusenciaView extends BaseView {

    void mostrarInfoNoPeticionesAusencia();

    void agregarAusenciasAadaptador(ArrayList<Ausencia> list);

    void mostrarFalloLecturaBaseDeDatos();
}
