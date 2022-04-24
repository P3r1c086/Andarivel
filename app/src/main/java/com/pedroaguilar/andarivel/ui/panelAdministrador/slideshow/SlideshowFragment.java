package com.pedroaguilar.andarivel.ui.panelAdministrador.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedroaguilar.andarivel.databinding.FragmentSlideshowBinding;


public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);*/

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        //Obtenemos los datos del usuario
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //si el usuario existe
                if(snapshot.exists()){
                    //obtengo los datos de firebase
                    //String uid = "" + snapshot.child("id").getValue(); si quiero sacar el id
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String apellidos = "" + snapshot.child("apellidos").getValue();
                    String direccion = "" + snapshot.child("direccion").getValue();
                    String nombreUsuario = "" + snapshot.child("nombreUsuario").getValue();
                   // String imagenPerfil = "" + snapshot.child("imagen").getValue();//en el caso de meter la imagen en la base de datos

                    //seteo los datos en los textView e imageView
                    binding.tvNombrePerfil.setText(nombre);
                    binding.tvApellidosPerfil.setText(apellidos);
                    binding.tvDireccionPerfil.setText(direccion);
                    binding.tvTelefonoPerfil.setText(nombreUsuario);

                    //para obtener la imagen
                  /*  try {
                        //si existe imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.foto_perfil).into.(ImagenDato);
                    }catch (Exception e){
                        //si no existe imagen
                        Picasso.get().load(R.drawable.foto_perfil).into(ImagenDato);
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}