package com.pedroaguilar.andarivel.ui.panelAdministrador;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.ActivityPanelAdministradorBinding;

import java.util.Objects;


public class PanelAdministradorActivity extends AppCompatActivity {

    private ActivityPanelAdministradorBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPanelAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // Pasar cada ID de menú como un conjunto de Ids porque cada menú debe ser considerado como destinos de primer nivel.

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_nested);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.drawerLayout.closeDrawers();
                int routeId = navController.getCurrentDestination().getId();
                if (routeId == R.id.home_dest) binding.navView.getMenu().getItem(0).setChecked(true);
                else if (routeId == R.id.gallery_dest) binding.navView.getMenu().getItem(1).setChecked(true);
                else if (routeId == R.id.slideshow_dest) binding.navView.getMenu().getItem(2).setChecked(true);
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
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });
    }
}