package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.net.Uri;

import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

import java.io.File;

public interface NotificarAusenciaView extends BaseView {
    void showAusencia(Ausencia ausencia);

    void showNoHayAusencias();

    void viewDoc(File file);
}
