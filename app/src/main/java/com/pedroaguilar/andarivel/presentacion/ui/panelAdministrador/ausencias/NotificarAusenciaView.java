package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface NotificarAusenciaView extends BaseView {
    void showAusencia(Ausencia ausencia);
    void showNoHayAusencias();
}
