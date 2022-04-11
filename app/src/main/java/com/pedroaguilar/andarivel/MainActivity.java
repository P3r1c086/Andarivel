package com.pedroaguilar.andarivel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button entrarApp;
    TextView info;
    //BaseDatosFirebase bd = new BaseDatosFirebase();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lanzarLoginActivity();
        //bd.escribirEnBd();
        //leerBd();

    }
    private void lanzarLoginActivity(){
        entrarApp = (Button) findViewById(R.id.btEntrarApp);
        Intent i = new Intent(this,Login.class);
        entrarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }
    private void leerBd(){
        info = (TextView) findViewById(R.id.info);
        myRef = FirebaseDatabase.getInstance().getReference().child("empleados");
        // Leer de la base de datos
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Usuario user = dataSnapshot.getValue(Usuario.class);
                info.setText(user.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                info.setText("Error al conectar");
            }
        });
    }
}