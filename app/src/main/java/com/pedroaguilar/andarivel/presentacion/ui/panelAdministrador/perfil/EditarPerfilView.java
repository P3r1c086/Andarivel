package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.perfil;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface EditarPerfilView extends BaseView {
    void showToastActualizadoCorrectamente();
    void navegarAperfil();
    void showTelefonoNoValido();
    void mostrarErrorRequeridoNombre();
    void mostrarErrorRequeridoApellido();
    void mostrarErrorRequeridoDireccion();
    void mostrarErrorRequeridoTelefono();
}
