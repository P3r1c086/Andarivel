package com.pedroaguilar.andarivel.ui.panelAdministrador.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedroaguilar.andarivel.databinding.FragmentSlideshowBinding;


public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReferenceUsuarios =  FirebaseDatabase.getInstance().getReference("Usuarios");;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);*/

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    /**
     * En este método se crea la lógica. Se inicializa una vez generada la vista con el onCreateView()
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReferenceUsuarios.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    //obtengo los datos de firebase
                    //String uid = "" + snapshot.child("id").getValue(); si quiero sacar el id
                    String nombre = "" + task.getResult().child("nombre").getValue();
                    String apellidos = "" + task.getResult().child("apellidos").getValue();
                    String direccion = "" + task.getResult().child("direccion").getValue();
                    String nombreUsuario = "" + task.getResult().child("nombreUsuario").getValue();
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
                } else {
                    Toast.makeText(getContext(), "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}