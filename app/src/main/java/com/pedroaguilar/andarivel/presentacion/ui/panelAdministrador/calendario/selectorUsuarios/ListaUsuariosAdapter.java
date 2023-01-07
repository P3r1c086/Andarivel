package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroaguilar.andarivel.R;

import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.calendario.selectorUsuarios
 * Create by Pedro Aguilar Fernández on 29/12/2022 at 18:24
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UserEvenViewHolder> {

    private ArrayList<String> listEmails;
    private ArrayList<String> listaEmailsChecked = new ArrayList<>();
    private ListaUsuariosPresenter presenter = new ListaUsuariosPresenter();

    public ListaUsuariosAdapter(ArrayList<String> list) {
        this.listEmails = list;
    }

    @NonNull
    @Override
    public ListaUsuariosAdapter.UserEvenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignar el diseño de los elemetos de la lista al recurso vista e departamentos layout
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarios_evento_item, parent, false);
        return new UserEvenViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UserEvenViewHolder holder, int position) {

        holder.email.setText(listEmails.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    listaEmailsChecked.add(holder.email.getText().toString());
                } else {
                    listaEmailsChecked.remove(holder.email.getText().toString());
                }
                presenter.agregarEmailsEvento(listaEmailsChecked);

//                if (holder.checkBox.isChecked()){
//                    listaEmailsChecked.add(holder.email.getText().toString());
//                }else {
//                    listaEmailsChecked.remove(holder.email.getText().toString());
//                }
//                presenter.agregarEmailsEvento(listaEmailsChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEmails.size();
    }


    public static class UserEvenViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView email;

        public UserEvenViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.cbUserEvent);
            email = itemView.findViewById(R.id.tvEmailUserEvent);
        }

    }
}
