package com.pedroaguilar.andarivel.ui.panelAdministrador.informes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Usuario;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.UsuarioViewHolder> {


    private ArrayList<Usuario> listaUsuarios;


    public Adaptador(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;

    }

    @NonNull
    @Override
    public Adaptador.UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignar el diseño de los elemetos de la lista a el recurso vista e departamentos layout
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.informe_item,null, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.UsuarioViewHolder holder, int position) {
        // asiganacion de los elementos del componente antes tienen que estar deados de alta como elementos de UsuarioViewHolder de abajo
        //son los metodos de tu entidad Usuario
        Context context = holder.itemView.getContext();
        holder.fecha.setText(listaUsuarios.get(position).getFecha());
        //holder.nombreUsuario.setText(context.getString(R.string.informes_fragment_name_text, listaUsuarios.get(position).getNombre()));
        //holder.ausencia.setText(Double.toString(listaUsuarios.get(position).getPresupuesto()));
        holder.horaEntrada.setText((listaUsuarios.get(position).getHoraEntrada()));
        holder.horaSalida.setText((listaUsuarios.get(position).getHoraSalida()));
        //holder.horasExtras.setText(Double.toString(listaUsuarios.get(position).getGastos()));
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        //Necesita saber el tamaño de la lista
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView fecha, nombreUsuario, ausencia, horaEntrada, horaSalida, horasExtras;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            fecha = itemView.findViewById(R.id.tvFechaDato);
            //nombreUsuario = itemView.findViewById(R.id.tvNombreUsuarioDato);
            //ausencia = itemView.findViewById(R.id.tvAusencia);
            horaEntrada = itemView.findViewById(R.id.tvHoraEntrada);
            horaSalida = itemView.findViewById(R.id.tvHoraSalida);
           // horasExtras = itemView.findViewById(R.id.tvHorasExtras);
        }
    }
}