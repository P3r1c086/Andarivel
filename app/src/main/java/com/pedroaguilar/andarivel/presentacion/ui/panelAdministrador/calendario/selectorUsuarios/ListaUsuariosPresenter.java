package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios;

import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios
 * Create by Pedro Aguilar Fernández on 29/12/2022 at 19:15
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class ListaUsuariosPresenter extends BasePresenter<ListaUsuariosView> {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private ArrayList<String> listaEmailUser = new ArrayList<>();

    public void leerEmailUsuarios() {
        database.getInfoUsers(task1 -> {
            if (task1.isSuccessful()) {
                //Obtenemos un mapa con todos los usuarios.
                Map<String, Object> mapRaiz1 = (Map<String, Object>) task1.getResult().getValue();
                //Recorremos la informacion de todos los usuarios.
                for (Map.Entry<String, Object> entry : mapRaiz1.entrySet()) {
                    //Obtenemos un mapa con los datos de los fichajes.
                    Map<String, String> mapInfoTodosUsuarios = (Map<String, String>) entry.getValue();
                    String email = new String();
                    email = mapInfoTodosUsuarios.get("email");
                    listaEmailUser.add(email);
                }
                view.agnadirListaEmail(listaEmailUser);
            }
        });
    }
}
