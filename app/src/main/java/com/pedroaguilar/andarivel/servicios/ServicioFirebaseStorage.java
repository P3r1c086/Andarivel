package com.pedroaguilar.andarivel.servicios;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Esta clase externaliza la conexion al Storage de Firebase
 */
public class ServicioFirebaseStorage {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();

    public void guardarImagenDePerfil(String userID, byte[] data, OnFailureListener failureListener, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        UploadTask uploadTask = storageReference.child("imagenesPerfil/" + userID + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(failureListener).addOnCompleteListener(onCompleteListener);
    }

    public void guardarDocumentoAusencia(String userID, byte[] data, String nombreAusencia, OnFailureListener failureListener, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        UploadTask uploadTask = storageReference.child("documentosAusencias/" + userID + "/" + nombreAusencia + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(failureListener).addOnCompleteListener(onCompleteListener);
    }

    public void guardarDocumentoAnuncio(String randomId, byte[] data, OnFailureListener failureListener, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        UploadTask uploadTask = storageReference.child("documentoAnuncio/" + randomId + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(failureListener).addOnCompleteListener(onCompleteListener);
    }

    public StorageReference getUserPerfilUrl(String userID) {
        return storage.getReferenceFromUrl("gs://andarivel-ficha.appspot.com/imagenesPerfil/" + userID + ".jpg");
    }

    public StorageReference getAnuncioUrl(String randomId) {
        return storage.getReferenceFromUrl("gs://andarivel-ficha.appspot.com/documentoAnuncio/" + randomId + ".jpg");
    }

    public void borrarFotoPerfil(String userID) {
        storageReference.child("imagenesPerfil/" + userID + ".jpg").delete();
    }

    public StorageReference getDocumentoAdjuntoAAusenciaUrl(String userID, String ausenciaId) {
        return storage.getReferenceFromUrl("gs://andarivel-ficha.appspot.com/documentosAusencias/" + userID + "/" + ausenciaId + ".jpg");
    }

    public void descargarYVerDocumentoAdjunto(File fileTemp, String userID, String ausenciaId, OnCompleteListener<FileDownloadTask.TaskSnapshot> onCompleteListener) {
        StorageReference docRef = storageReference.child("documentosAusencias/" + userID + "/" + ausenciaId);
        docRef.getFile(fileTemp).addOnCompleteListener(onCompleteListener);
    }

    public void descargarYVerAnuncio(File fileTemp, String randomId, OnCompleteListener<FileDownloadTask.TaskSnapshot> onCompleteListener) {
        StorageReference docRef = storageReference.child("documentoAnuncio/" + randomId);
        docRef.getFile(fileTemp).addOnCompleteListener(onCompleteListener);
    }
}
