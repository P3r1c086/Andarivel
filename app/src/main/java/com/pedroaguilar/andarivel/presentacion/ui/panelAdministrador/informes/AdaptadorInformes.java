package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.informes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Fichaje;

import java.util.ArrayList;

public class AdaptadorInformes extends RecyclerView.Adapter<AdaptadorInformes.UsuarioViewHolder> {


    private ArrayList<Fichaje> listaFichaje;


    public AdaptadorInformes(ArrayList<Fichaje> lista) {
        this.listaFichaje = lista;

    }

    @NonNull
    @Override
    public AdaptadorInformes.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignar el diseño de los elemetos de la lista al recurso vista e departamentos layout
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.informe_item,parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInformes.UsuarioViewHolder holder, int position) {
        // asiganacion de los elementos del componente, antes tienen que estar dados de alta como elementos de UsuarioViewHolder de abajo,
        //son los metodos de tu entidad Usuario
        Context context = holder.itemView.getContext();
        holder.fecha.setText(listaFichaje.get(position).getFecha());
        holder.nombreUsuario.setText(context.getString(R.string.informes_fragment_name_text, listaFichaje.get(position).getNombreUsuario()));
        holder.horaEntrada.setText((listaFichaje.get(position).getHoraEntrada()));
        holder.horaSalida.setText((listaFichaje.get(position).getHoraSalida()));
        holder.tiempoTrabajado.setText((listaFichaje.get(position).getTiempoTrabajadoDia()));
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        //Necesita saber el tamaño de la lista
        return listaFichaje.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView fecha, nombreUsuario, horaEntrada, horaSalida, tiempoTrabajado;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.tvFechaDato);
            nombreUsuario = itemView.findViewById(R.id.tvNombreUsuarioDato);
            horaEntrada = itemView.findViewById(R.id.tvHoraEntrada);
            horaSalida = itemView.findViewById(R.id.tvHoraSalida);
            tiempoTrabajado = itemView.findViewById(R.id.tvTiempoTrabajadoDiaDato);
        }

        //todo: complemetar metodo proveniente del servicio firebase database para borrar informe(en nodo Fichaje y Usuarios) con
        //un onLongClick(). Poner dialog para confirmar
    }
}