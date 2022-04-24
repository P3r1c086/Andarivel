package com.pedroaguilar.andarivel;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.modelo.Usuario;

public class BaseDatosFirebase extends Activity{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public void escribirEnBd(){
        // Escribir un mensaje en la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");

       /* Bundle extras = getIntent().getExtras();
        user.setHorasDescanso(250.5);
        user.setID(1);
        user.setApellidos(extras.getString("apellidos")) ;
        user.setDepartamento("contabilidad");
        user.setDiasVacaciones(12.2);
        user.setDireccion(extras.getString("direccion")) ;
        user.setEmail(extras.getString("email")) ;
        user.setFoto("foto.jpg");
        user.setHorasTrabajadas(350.5); ;
        user.setMotivoAusencia("enfermedad") ;
        user.setNombre(extras.getString("nombre")) ;
        user.setNombreUsuario(extras.getString("nombreUsuario")) ;
        user.setPassword(extras.getString("pass")) ;
        user.setPuesto("contable") ;
        user.setRol("Administrador") ;
        user.setTelefono("671033943") ;


        myRef.setValue(user.getHorasDescanso());
        myRef.setValue(user.getID());
        myRef.setValue(user.getApellidos());
        myRef.setValue(user.getDepartamento());
        myRef.setValue(user.getDiasVacaciones());
        myRef.setValue(user.getDireccion());
        myRef.setValue(user.getEmail());
        myRef.setValue(user.getFoto());
        myRef.setValue(user.getHorasTrabajadas());
        myRef.setValue(user.getMotivoAusencia());
        myRef.setValue(user.getNombre());
        myRef.setValue(user.getNombreUsuario());
        myRef.setValue(user.getPassword());
        myRef.setValue(user.getPuesto());
        myRef.setValue(user.getRol());
        myRef.setValue(user.getTelefono());*/


    }
    public void leerEnBd(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        // Leer de la base de datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String datos = null;
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String dato = dataSnapshot.child("3dc907c3-46ea-46b5-9fd9-b39784dd7255").getValue().toString();
                //Log.d(TAG, "Value is: " + value);
                Usuario user = dataSnapshot.getValue(Usuario.class);
                datos = (user.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void leerbd(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("///////////////////////////////////////////////////////", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("///////////////////////////////////////////////////////", "Failed to read value.", error.toException());
            }
        });
    }
}
