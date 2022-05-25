package com.pedroaguilar.andarivel.servicios;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ServicioFirebaseStorage {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public void guardarImagenDePerfil(String userID, byte[] data, OnFailureListener failureListener, OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener){
        UploadTask uploadTask = storageReference.child("imagenesPerfil/" + userID +".jpg").putBytes(data);
        uploadTask.addOnFailureListener(failureListener).addOnCompleteListener(onCompleteListener);
    }

    public StorageReference getUserPerfilUrl(String userID){
        return storage.getReferenceFromUrl("gs://andarivel-ficha.appspot.com/imagenesPerfil/" + userID +".jpg");
    }


}
