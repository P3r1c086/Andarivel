package com.pedroaguilar.andarivel.presentacion.comun.presenter;

import com.pedroaguilar.andarivel.presentacion.comun.view.BaseView;

/**
 * Abstract class father for all the presenters on the proyect.
 * It stores the view associated to.
 *
 *
 * -> MVP pattern (Model View Presenter) that is base
 *  on the idea that we have a Model (with the data) - a Presenter that knows the Model and the View - and View which only
 *  contains the Android classes and prints the data on the screen. The Presenter is the bridge between the Model and the view.
 *  The main idea to have a class Presenter is to isolate the Model from the View and vice versa. So the flow of data is from
 *  the Model to the Presenter and the arrives to the view, and in the other sense the same flow.
 *
 * @param <T>
 */
public abstract class BasePresenter<T extends BaseView> implements Presenter<T> {

    //Attribute that store the view which can be accesses from the children
    protected T view = null;

    @Override
    public void initialize(T view) {
        this.view = view;
    }

    //Destroys the view setting to null
    @Override
    public void dispose() {
        this.view = null;
    }
}
