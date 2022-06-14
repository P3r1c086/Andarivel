package com.pedroaguilar.andarivel.presentacion.comun.presenter;


import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

/**
 * Presenter base which the BasePresenter has to implement
 * @param <T>: The View
 */
public interface Presenter<T extends BaseView> {

    public void initialize(T view);

    public void dispose();

}
