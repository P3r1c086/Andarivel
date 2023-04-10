package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosPresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador
 * Create by Pedro Aguilar Fernández on 14/12/2022 at 13:48
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class TablonAnunciosAdminPresenter extends CamaraYpermisosPresenter<TablonAnunciosAdminView> {

    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    protected String id = generarRandomId();//era null
    File localDoc = null;

    public String generarRandomId() {
        id = ref.push().getKey();
        return id;
    }

    public void botonAceptarClickado(String title, String description, String id, String link) {
        if (validarTitle(title)) {
            guardarDatosAnuncio(title, description, id, link);
            view.mostrarExitoSubidaAnuncio();
        }
    }

    public boolean validarTitle(String title) {
        boolean resultado = true;
        if (title.isEmpty()) {
            view.mostrarErrorRequeridoTitle();
            resultado = false;
        }
        return resultado;
    }

    public void guardarDatosAnuncio(String title, String description, String id, String link) {
        Anuncio anuncio = new Anuncio();
        if (id != null) {
//            anuncio.setImgUrl(id + ".jpg");
            //todo: el id nunca va a ser "", tengo que hacerlo de otra forma. Igual hacer una
            // busqueda en el storage y compararlo con el id, ya que al seleccionar una foto ya se
            // ha introducido en el storage con ese id
//            if (id.equals("")){
//                anuncio.setImgUrl("");
//            }
            //todo: esto no funciona, Tengo que crear un metodo en el storage que lea el nombre de la imagenes que tengo
            if (storage.getAnuncioUrl(id).toString().contains(id + ".jpg")) {
                anuncio.setImgUrl(id + ".jpg");
            } else {
                anuncio.setImgUrl("");
            }
            if (!link.equals("")) {
                anuncio.setLink(link);
            } else {
                anuncio.setLink("");
            }
            anuncio.setId(id);
            anuncio.setTitle(title);
            anuncio.setDescripcion(description);
            database.saveAnuncio(id, anuncio);
        } else {
            view.mostrarError();
        }

    }

    public void setImagenUser() {
        view.setImagenUsuario(storage.getAnuncioUrl(id));
    }

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
                        anuncio.setLink(mapAnuncio.get("link"));
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
