package com.pedroaguilar.andarivel.presentacion.ui.comunUi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;
import com.pedroaguilar.andarivel.presentacion.comun.presenter.BasePresenter;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

public class CamaraYpermisosPresenter<T extends CamaraYpermisosView> extends BasePresenter<T> {

    protected final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    protected byte[] imagen = null;

    protected void guardaImagenPerfil(byte[] data) {
        imagen = data;
    }

    protected void guardarDocumentoAusenciaEnStorage(String nombreAusencia) {
        storage.guardarDocumentoAusencia(FirebaseAuth.getInstance().getUid(), imagen, nombreAusencia,
                e -> view.showErrorSubirImagen(),
                task -> {
                });
    }

    public void guardarImagenPerfil(OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        storage.guardarImagenDePerfil(FirebaseAuth.getInstance().getUid(), imagen,
                e -> view.showErrorSubirImagen(), onCompleteListener);

    }
}
