package com.pedroaguilar.andarivel.presentacion.ui.login;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

public interface NuevoUsuarioView extends BaseView {
    void setErrorEmail();

    void showErrorEmail();

    void showErrorDebesIntroducirContrasena();

    void showErrorContrasenaDebil();

    void showErrorTlfNoValido();

    void mostrarErrorRequeridoNombre();

    void mostrarErrorRequeridoApellido();

    void mostrarErrorRequeridoDireccion();

    void mostrarErrorRequeridoCorreo();

    void mostrarErrorRequeridoTelefono();

    void mostrarErrorRequeridoPass();

    void toastEmailYaRegistrado();

    void falloAutenticacion();

    void navegarActividadMenu();
}
