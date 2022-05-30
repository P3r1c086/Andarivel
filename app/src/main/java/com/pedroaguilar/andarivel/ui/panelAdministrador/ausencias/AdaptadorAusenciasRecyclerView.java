package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Ausencia;

import java.util.ArrayList;

public class AdaptadorAusenciasRecyclerView extends RecyclerView.Adapter<AdaptadorAusenciasRecyclerView.UsuarioViewHolder>{

    private ArrayList<Ausencia> listaAusencia;


    public AdaptadorAusenciasRecyclerView(ArrayList<Ausencia> lista) {
        this.listaAusencia = lista;

    }

    @NonNull
    @Override
    public AdaptadorAusenciasRecyclerView.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignar el diseño de los elemetos de la lista a el recurso vista e departamentos layout
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.ausencia_item,parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorAusenciasRecyclerView.UsuarioViewHolder holder, int position) {
        // asiganacion de los elementos del componente antes tienen que estar deados de alta como elementos de UsuarioViewHolder de abajo
        //son los metodos de tu entidad Usuario
        Context context = holder.itemView.getContext();
        holder.motivo.setText(" " + listaAusencia.get(position).getMotivoAusencia());
        holder.nombreUsuario.setText(" " + listaAusencia.get(position).getNombreUsuario());
        holder.fechaI.setText(listaAusencia.get(position).getFechaInicioAusencia());
        holder.fechaF.setText(listaAusencia.get(position).getFechaFinAusencia());
        holder.descripcion.setText(" " + listaAusencia.get(position).getDescripcionAusencia());
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        //Necesita saber el tamaño de la lista
        return listaAusencia.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView motivo, nombreUsuario, fechaI, fechaF, descripcion;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            motivo = itemView.findViewById(R.id.tvMotivoAusenciaDato);
            nombreUsuario = itemView.findViewById(R.id.tvNombreUsuarioAusenciaDato);
            fechaI = itemView.findViewById(R.id.tvFechaInicioAusencia);
            fechaF = itemView.findViewById(R.id.tvFechaFinAusencia);
            descripcion = itemView.findViewById(R.id.tvDescripcionAusenciaDato);
        }
    }
}
