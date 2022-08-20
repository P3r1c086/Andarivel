package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosView;

public interface SolicitarAusenciaView extends CamaraYpermisosView {
    void showAusenciaPendiente();

    void showAusenciaCreadaConExito();

    void showErrorMotivoAusencia();

    void showErrorFechaIncio();

    void showErrorFechaFin();
}
