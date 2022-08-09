package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface SolicitarAusenciaView extends BaseView {
    void showAusenciaPendiente();

    void showAusenciaCreadaConExito();

    void showErrorMotivoAusencia();

    void showErrorFechaIncio();

    void showErrorFechaFin();

    void showErrorSubirImagen();
}
