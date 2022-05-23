package com.pedroaguilar.andarivel.ui.panelAdministrador.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.databinding.FragmentInformesBinding;
import com.pedroaguilar.andarivel.modelo.Constantes;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.ArrayList;


public class InformesFragment extends Fragment  {
    private FragmentInformesBinding binding;
    private RecyclerView listaTrabajadores;
    private ArrayList<Usuario> arrayListUsuarios = new ArrayList<Usuario>();
   // private ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    //private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference(Constantes.TABLA_USUARIOS);
    private Adaptador adaptador = new Adaptador(arrayListUsuarios);

    public InformesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaTrabajadores = binding.listaUsuarios;
        listaTrabajadores.setLayoutManager(new LinearLayoutManager(getContext()));
//        database.leerUsuario(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    //obtengo los datos de firebase
//                    //String uid = "" + snapshot.child("id").getValue(); si quiero sacar el id
//                    String nombre = "" + task.getResult().child("nombre").getValue();
//                    String apellidos = "" + task.getResult().child("apellidos").getValue();
////                    String direccion = "" + task.getResult().child("direccion").getValue();
////                    String telefono = "" + task.getResult().child("telefono").getValue();
////                    String email = "Email: " + task.getResult().child("email").getValue();
//                    // String imagenPerfil = "" + snapshot.child("imagen").getValue();//en el caso de meter la imagen en la base de datos
//
//                    //seteo los datos en el usuario
//                    Usuario us = new Usuario();
//                    us.setNombre(nombre.concat(" " + apellidos));
////                    us.setHoraEntrada("05/05/2022");
////                    us.setHoraSalida("Salimos");
//                    arrayListUsuarios.add(us);
//                    Adaptador adaptador = new Adaptador(arrayListUsuarios);
//                    listaTrabajadores.setAdapter(adaptador);
//                    //binding.tvNombreCompletoPerfil.setText(nombre.concat(" " + apellidos));
////                    binding.tvDireccionPerfil.setText(direccion);
////                    binding.tvTelefonoPerfil.setText(telefono);
////                    binding.tvEmailPerfil.setText(email);
//                    //para obtener la imagen
//              /*  try {
//                    //si existe imagen
//                    Picasso.get().load(imagen).placeholder(R.drawable.foto_perfil).into.(ImagenDato);
//                }catch (Exception e){
//                    //si no existe imagen
//                    Picasso.get().load(R.drawable.foto_perfil).into(ImagenDato);
//                    }*/
//                } else {
//                    Toast.makeText(getContext(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        Usuario us = new Usuario();
//        us.setNombre("Pepito");
//        us.setHoraEntrada("05/05/2022");
//        us.setHoraSalida("Salimos");
//        arrayListUsuarios.add(us);
//        Adaptador adaptador = new Adaptador(arrayListUsuarios);
//        listaTrabajadores.setAdapter(adaptador);
        databaseReferenceUsuarios = FirebaseDatabase.getInstance().getReference();
        leerTodosUsuariosDatabase();
        //Adaptador adapter = new Adaptador(leerUsuarios());
        //listaTrabajadores.setAdapter(adapter);
    }

    public void leerTodosUsuariosDatabase(){

        databaseReferenceUsuarios.child("Fichaje").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Usuario user = ds.getValue(Usuario.class);
                        arrayListUsuarios.add(user);
                    }
                    adaptador = new Adaptador(arrayListUsuarios);
                    listaTrabajadores.setAdapter(adaptador);
                }else{
                    Toast.makeText(getContext(), "fallo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//    public ArrayList<Usuario> leerUsuarios(){
//
//        leerTodosUsuariosDatabase();
////        arrayListUsuarios.add(new Usuario("Pedro"));
////        arrayListUsuarios.add(new Usuario("Angel"));
////        arrayListUsuarios.add(new Usuario("Mario"));
////        arrayListUsuarios.add(new Usuario("Lorena"));
////        arrayListUsuarios.add(new Usuario("Maria"));
////        arrayListUsuarios.add(new Usuario("Emma"));
//        return  arrayListUsuarios;
//    }
}