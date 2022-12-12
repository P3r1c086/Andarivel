package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.StorageReference;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.presentacion.ui.login.LoginActivity;

import java.util.Objects;
import java.util.UUID;

/**
 * Acitividad principal de la aplicacion que contiene un nuevo grafo de navegacion que ademas
 * se integra con el drawerLayout y con la toolbar integrada en su xml. Con lo que conseguimos que
 * el icono del menu y el titulo que se muestra en la toolbar cambien conforme cambia la navegacion.
 */
public class PanelAdministradorActivity extends AppCompatActivity implements PanelAdministradorView {

    private final PanelAdministradorPresenter presenter = new PanelAdministradorPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.initialize(this);
        presenter.logicaPanelAdministrador();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void inflarVistaSegunPerfil(Boolean esAdmin) {
        if (esAdmin != null && esAdmin) {
            setContentView(R.layout.activity_panel_administrador);
        } else {
            setContentView(R.layout.activity_panel_no_administrador);
        }
        setListeners();
    }

    private void setListeners() {
        NavigationView navView = findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Pasar cada ID de menú como un conjunto de Ids porque cada menú debe ser considerado como destinos de primer nivel.

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_nested);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        //Cuando cambie la navegación, gracias al listener, esta parte salta.
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            //se almacena el id de la ruta para actualizar el elemento del menu
            int routeId = navController1.getCurrentDestination().getId();
            if (routeId == R.id.home_dest)
                navView.getMenu().getItem(0).setChecked(true);
            else if (routeId == R.id.calendario_dest)
                navView.getMenu().getItem(1).setChecked(true);
            else if (routeId == R.id.perfil_dest)
                navView.getMenu().getItem(2).setChecked(true);
            else if (routeId == R.id.ausencias_dest)
                navView.getMenu().getItem(3).setChecked(true);
            else if (routeId == R.id.informes_dest)
                navView.getMenu().getItem(4).setChecked(true);
            else if (routeId == R.id.trabajadores_dest)
                navView.getMenu().getItem(5).setChecked(true);
            else if (routeId == R.id.tablonAnuncios_dest)
                navView.getMenu().getItem(4).setChecked(true);
            else if (routeId == R.id.settings_dest)
                navView.getMenu().getItem(5).setChecked(true);
            else if (routeId == R.id.contacto_dest)
                navView.getMenu().getItem(6).setChecked(true);
        });
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .setOpenableLayout(drawerLayout)
                .build();
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
        navView.setNavigationItemSelectedListener(item -> {
            //Cuando clicamos siempre cierra el drawer
            drawerLayout.closeDrawers();
            return NavigationUI.onNavDestinationSelected(item, navController);
        });
        View header = navView.getHeaderView(0);
        //Al pulsar en la cabecera, cerramos el Drawer y navegamos al fragmento del perfil.
        header.setOnClickListener(view -> {
            drawerLayout.closeDrawers();
            navController.navigate(R.id.perfil_dest);
        });
        //Realizamos esto para actualizar los campos imagen y nombre en la cabecera si el usuario los actualiza.
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                presenter.loadHeaderInfoUser();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void navegarAlLogin() {
        Intent intent = new Intent(PanelAdministradorActivity.this, LoginActivity.class);
        //Navegamos al loginActivity y finalizamos la actividad de PanelAdministradorActivity
        startActivity(intent);
        finish();
    }

    @Override
    public void setImageHeaderDrawer(StorageReference url) {
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(PanelAdministradorActivity.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        //para obtener la imagen usamos la libreria de Glide
        GlideApp.with(getApplicationContext())
                .load(url)
                .placeholder(circularProgressDrawable)
                .circleCrop()
                .error(R.mipmap.ic_launcher)
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into((ImageView) header.findViewById(R.id.imgFotoPerfil));
    }

    @Override
    public void setTextHeaderDrawer(String nombre, String apellidos, String email) {
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.tvNombreCompleto)).setText(nombre + " " + apellidos);
        ((TextView) header.findViewById(R.id.tvCorreo)).setText(email);
    }
}