package com.pedroaguilar.andarivel.ui.panelAdministrador;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.pedroaguilar.andarivel.GlideApp;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;
import com.pedroaguilar.andarivel.ui.login.LoginActivity;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Acitividad principal de la aplicacion que contiene un nuevo grafo de navegacion que ademas
 * se integra con el drawerLayout y con la toolbar integrada en su xml. Con lo que consigo que
 * el icono del menu y el titulo que se muestra en la toolbar cambien conforme cambia la navegacion.
 */
public class PanelAdministradorActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            database.getInfoUser(firebaseAuth.getUid(), new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Boolean esAdmin = (Boolean) ((Map<String, Object>) task.getResult().getValue()).get("esAdiminstrador");

                        if (esAdmin != null && esAdmin) {
                            setContentView(R.layout.activity_panel_administrador);
                        } else {
                            setContentView(R.layout.activity_panel_no_administrador);
                        }

                        NavigationView navView = findViewById(R.id.nav_view);
                        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        TextView logout = findViewById(R.id.logout);

                        // Pasar cada ID de menú como un conjunto de Ids porque cada menú debe ser considerado como destinos de primer nivel.

                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_nested);
                        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

                        //Cuando cambie la navegación, gracias al listener, esta parte salta.
                        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                            @Override
                            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                                //se almacena el id de la ruta para actualizar el elemento del menu
                                int routeId = navController.getCurrentDestination().getId();
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
                            }
                        });

                        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                                navController.getGraph())
                                .setOpenableLayout(drawerLayout)
                                .build();
                        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
                        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                //Cuando clicamos siempre cierra el drawer
                                drawerLayout.closeDrawers();
                                return NavigationUI.onNavDestinationSelected(item, navController);
                            }
                        });
                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(PanelAdministradorActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        View header = navView.getHeaderView(0);
                        header.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                drawerLayout.closeDrawers();
                                navController.navigate(R.id.perfil_dest);
                            }
                        });

                        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                            @Override
                            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                            }

                            @Override
                            public void onDrawerOpened(@NonNull View drawerView) {
                                //para obtener la imagen usamos la libreria de Glide
                                GlideApp.with(getApplicationContext())
                                        .load(storage.getUserPerfilUrl(firebaseAuth.getUid()))
                                        .circleCrop()
                                        .error(R.mipmap.ic_launcher)
                                        .signature(new ObjectKey(UUID.randomUUID().toString()))
                                        .into((ImageView) header.findViewById(R.id.imgFotoPerfil));
                            }

                            @Override
                            public void onDrawerClosed(@NonNull View drawerView) {

                            }

                            @Override
                            public void onDrawerStateChanged(int newState) {

                            }
                        });

                        if (firebaseAuth != null) {
                            database.getInfoUser(firebaseAuth.getUid(), new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        String nombre = "" + task.getResult().child("nombre").getValue();
                                        String apellidos = "" + task.getResult().child("apellidos").getValue();
                                        String email = "" + task.getResult().child("email").getValue();

                                        //seteo los datos en los textView e imageView
                                        ((TextView) header.findViewById(R.id.tvNombreCompleto)).setText(nombre + " " + apellidos);
                                        ((TextView) header.findViewById(R.id.tvCorreo)).setText(email);

                                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(PanelAdministradorActivity.this);
                                        circularProgressDrawable.setStrokeWidth(5f);
                                        circularProgressDrawable.setCenterRadius(30f);
                                        circularProgressDrawable.start();

                                        //para obtener la imagen usamos la libreria de Glide
                                        GlideApp.with(getApplicationContext())
                                                .load(storage.getUserPerfilUrl(firebaseAuth.getUid()))
                                                .placeholder(circularProgressDrawable)
                                                .circleCrop()
                                                .error(R.mipmap.ic_launcher)
                                                .signature(new ObjectKey(UUID.randomUUID().toString()))
                                                .into((ImageView) header.findViewById(R.id.imgFotoPerfil));

                                    }
                                }
                            });
                        }

                    }
                }
            });


        }
    }

}