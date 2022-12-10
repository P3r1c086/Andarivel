package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.informes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.databinding.FragmentInformesBinding;
import com.pedroaguilar.andarivel.modelo.Fichaje;

import java.io.IOException;
import java.util.ArrayList;


public class InformesFragment extends Fragment implements InformesView{

    private FragmentInformesBinding binding;
    private final InformesPresenter presenter = new InformesPresenter();

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
        presenter.initialize(this);
        //El LayoutManager es responsable de medir y posicionar las vistas de elementos dentro de un RecyclerView
        //así como de determinar la política sobre cuándo reciclar las vistas de elementos que ya no son visibles para el usuario.
        binding.listaUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.leerTodosUsuariosDatabase();
    }

    @Override
    public void agnadirListaUsuariosCompleta(ArrayList<Fichaje> list) {
        //Colocamos en el adaptador la lista de fichajes que continen todos los datos necesarios.
        binding.listaUsuarios.setAdapter(new AdaptadorInformes(list));
    }

    @Override
    public void mostrarListaVacia() {
        ArrayList<Fichaje> listaConVacio = new ArrayList<>();
        Fichaje fVacio = new Fichaje();
        //En caso de que no haya fichajes todavia, notificamos al administrador que no hay fichajes.
        fVacio.setNombreUsuario(getString(R.string.no_hay_fichajes));
        listaConVacio.add(fVacio);
        binding.listaUsuarios.setAdapter(new AdaptadorInformes(listaConVacio));
    }

    @Override
    public void mostrarFalloFirebase() {
        Toast.makeText(getContext(), R.string.fallo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mostrarExitoExcel() {
        Toast.makeText(getContext(), R.string.exito_descarga_excel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadDataExcel(ArrayList<Fichaje> list) {
        binding.btnDownloadExcel.setOnClickListener(view2 -> {
            try {
                presenter.downloadExcel(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}