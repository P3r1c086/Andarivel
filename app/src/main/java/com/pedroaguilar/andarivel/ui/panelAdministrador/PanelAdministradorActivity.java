package com.pedroaguilar.andarivel.ui.panelAdministrador;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.ActivityPanelAdministradorBinding;
import com.pedroaguilar.andarivel.ui.login.LoginActivity;

import java.time.Instant;
import java.util.Objects;

/**
 * Acitividad principal de la aplicacion que contiene un nuevo grafo de navegacion que ademas
 * se integra con el drawerLayout y con la toolbar integrada en su xml. Con lo que consigo que
 * el icono del menu y el titulo que se muestra en la toolbar cambien conforme cambia la navegacion.
 */
public class PanelAdministradorActivity extends AppCompatActivity {

    private ActivityPanelAdministradorBinding binding;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private Instant Glide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPanelAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // Pasar cada ID de menú como un conjunto de Ids porque cada menú debe ser considerado como destinos de primer nivel.

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_nested);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        //Cuando cambie la navegación, gracias al listener, esta parte salta.
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                //se almacena el id de la ruta para actualizar el elemento del menu
                int routeId = navController.getCurrentDestination().getId();
                if (routeId == R.id.home_dest) binding.navView.getMenu().getItem(0).setChecked(true);
                else if (routeId == R.id.calendario_dest) binding.navView.getMenu().getItem(1).setChecked(true);
                else if (routeId == R.id.perfil_dest) binding.navView.getMenu().getItem(2).setChecked(true);
                else if (routeId == R.id.ausencias_dest) binding.navView.getMenu().getItem(3).setChecked(true);
                else if (routeId == R.id.informes_dest) binding.navView.getMenu().getItem(4).setChecked(true);
            }
        });

        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, mAppBarConfiguration);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Cuando clicamos siempre cierra el drawer
                binding.drawerLayout.closeDrawers();
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PanelAdministradorActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        View header = binding.navView.getHeaderView(0);
        header.findViewById(R.id.imgFotoPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PanelAdministradorActivity.this, "Has tocado la foto",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}