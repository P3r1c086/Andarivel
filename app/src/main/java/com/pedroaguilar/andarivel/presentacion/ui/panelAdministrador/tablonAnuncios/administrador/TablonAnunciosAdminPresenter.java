package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.administrador;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.presentacion.ui.comunUi.CamaraYpermisosPresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

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

    public String generarRandomId() {
        id = ref.push().getKey();
        return id;
    }

    public void botonAceptarClickado(String title, String description, String id, String link) {
        if (validarTitle(title)) {
//            guardarDatosAnuncio(anuncio);
            guardarDatosAnuncio(title, description, id, link);
            view.mostrarExitoSubidaAnuncio();
        }
    }

//    public void botonAceptarClickadoSiImage(String title, String description, String id){
//        if (validarTitle(title)){
////            guardarDatosAnuncio(anuncio);
//            guardarDatosAnuncioSiImage(title, description, id);
//            view.mostrarExitoSubidaAnuncio();
//        }
//    }

    public boolean validarTitle(String title) {
        boolean resultado = true;
        if (title.isEmpty()) {
            view.mostrarErrorRequeridoTitle();
            resultado = false;
        }
        return resultado;
    }

    //    public void guardarDatosAnuncio(Anuncio anuncio) {
//        database.saveAnuncio(id, anuncio);
//    }
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

    //    public void guardarDatosAnuncioSiImage(String title, String description, String id) {
//        Anuncio anuncio = new Anuncio();
//        if (id != null){
//            anuncio.setImgUrl(id + ".jpg");
//            anuncio.setId(id);
//            anuncio.setTitle(title);
//            anuncio.setDescripcion(description);
//            database.saveAnuncio(id, anuncio);
//        }else{
//            view.mostrarError();
//        }
//
//    }
    public void setImagenUser() {
        view.setImagenUsuario(storage.getAnuncioUrl(id));
    }
}
