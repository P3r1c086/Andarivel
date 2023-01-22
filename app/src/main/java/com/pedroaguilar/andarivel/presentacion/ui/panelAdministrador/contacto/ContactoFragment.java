package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.contacto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedroaguilar.andarivel.databinding.FragmentContactoBinding;

public class ContactoFragment extends Fragment implements ContactoView {

    private FragmentContactoBinding binding;
    private final ContactoPresenter presenter = new ContactoPresenter();

    private final Double lat = 0.0;
    private final Double lon = 0.0;

    private String nombre = "";
    private String apellidos = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.initialize(this);
        presenter.leerDatoUsuario();
        setupIntent();
    }

    @Override
    public void setDatosMailUsuario(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    private void setupIntent() {
        //enviar un correo
        binding.tvEmailDato.setOnClickListener(v -> {
            Uri mail = Uri.parse("mailto:"); // Creamos una uri con el numero de telefono
            Intent intent = new Intent(Intent.ACTION_SENDTO, mail);
            //configurar los argumentos
            //para configurar el destinatario o los destinatarios (array)
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pajaros33@gmail.com"});
            // esto configura el asunto
            intent.putExtra(Intent.EXTRA_SUBJECT, "De: " + nombre
                    + " " + apellidos);
            //este es para el mensaje
            //intent.putExtra(Intent.EXTRA_TEXT, "Hola, de parte de Andarivel, me gustaría informarle de:");

            // Lanzo el selector de cliente de Correo
            startActivity(Intent.createChooser(intent, "Elije un cliente de Correo:"));
        });
        binding.tvPhoneDato.setOnClickListener(v -> {
            String num = binding.tvPhoneDato.getText().toString(); // Guardamos el numero de telefono en un string

            Uri number = Uri.parse("tel:" + num); // Creamos una uri con el numero de telefono
            Intent dial = new Intent(Intent.ACTION_DIAL, number); // Creamos una llamada al Intent de llamadas
            startActivity(dial); // Ejecutamos el Intent
        });
        binding.tvLocationDato.setOnClickListener(v -> {
            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Andarivel@37.158125,-3.581334");

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);
        });
    }
}