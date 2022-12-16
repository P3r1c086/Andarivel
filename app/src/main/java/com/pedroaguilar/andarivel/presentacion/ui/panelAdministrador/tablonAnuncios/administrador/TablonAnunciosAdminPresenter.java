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
    protected String id = null;

    public String generarRandomId() {
        id = ref.push().getKey();
        return id;
    }

    public void guardarDatosAnuncio(Anuncio anuncio) {
        database.saveAnuncio(id, anuncio);
    }

    public void setImagenUser() {
        view.setImagenUsuario(storage.getAnuncioUrl(id));
    }
}
