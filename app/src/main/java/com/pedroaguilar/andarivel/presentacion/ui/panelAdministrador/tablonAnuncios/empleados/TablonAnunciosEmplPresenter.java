package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.storage.FileDownloadTask;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados
 * Create by Pedro Aguilar Fernández on 15/12/2022 at 19:07
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class TablonAnunciosEmplPresenter extends BasePresenter<TablonAnunciosEmplView> {
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    File localDoc = null;

    public void leerTodosAnunciosDatabase() {
        database.getInfoAnuncios(task -> {
            if (task.isSuccessful()) {
                ArrayList<Anuncio> listaAnunciosCompleta = new ArrayList<Anuncio>();
                //Obtenemos un mapa con todos los anuncios.
                Map<String, Object> mapRaiz1 = (Map<String, Object>) task.getResult().getValue();
                //Recorremos la informacion de todos los anuncios.
                if (mapRaiz1 != null && !mapRaiz1.isEmpty()) {
                    for (Map.Entry<String, Object> entry : mapRaiz1.entrySet()) {
                        //En este mapa obtenemos los datos de cada anuncio.
                        Map<String, String> mapAnuncio = (Map<String, String>) entry.getValue();
                        Anuncio anuncio = new Anuncio();
                        anuncio.setId(mapAnuncio.get("id"));
                        anuncio.setTitle(mapAnuncio.get("title"));
                        anuncio.setDescripcion(mapAnuncio.get("descripcion"));
                        anuncio.setImgUrl(mapAnuncio.get("imgUrl"));
                        listaAnunciosCompleta.add(anuncio);
                    }
                }
                view.agnadirListaAnunciosCompleta(listaAnunciosCompleta);
            }
        });
    }

    public void verImgAnuncio(File fileTemp, Anuncio anuncio, OnCompleteListener<FileDownloadTask.TaskSnapshot> onCompleteListener) {
        localDoc = fileTemp;
        storage.descargarYVerAnuncio(fileTemp, anuncio.getId(), onCompleteListener);
    }

}
