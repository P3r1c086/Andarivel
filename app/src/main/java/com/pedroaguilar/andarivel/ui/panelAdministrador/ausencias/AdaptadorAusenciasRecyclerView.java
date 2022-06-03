package com.pedroaguilar.andarivel.ui.panelAdministrador.ausencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorAusenciasRecyclerView extends RecyclerView.Adapter<AdaptadorAusenciasRecyclerView.UsuarioViewHolder>{

    private final ArrayList<Ausencia> listaAusencia;
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();

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
        String estado = listaAusencia.get(position).getEstado();
        holder.estado.setText(estado);
        if (!estado.equals("Pendiente")) {
            holder.aceptar.setVisibility(View.INVISIBLE);
            holder.denegar.setVisibility(View.INVISIBLE);
        } else {
            holder.aceptar.setVisibility(View.VISIBLE);
            holder.denegar.setVisibility(View.VISIBLE);
        }
        holder.aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+ listaAusencia.get(position).getIdAusencia() + "/estado", "Aceptada");
                database.actualizarAusencia(childUpdates, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listaAusencia.get(position).setEstado("Aceptada");
                            notifyItemChanged(position);
                            Toast.makeText(context, "Ausencia Aceptada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        holder.denegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+ listaAusencia.get(position).getIdAusencia() + "/estado", "Denegada");
                database.actualizarAusencia(childUpdates, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listaAusencia.get(position).setEstado("Denegada");
                            notifyItemChanged(position);
                            Toast.makeText(context, R.string.ausencia_denegada, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
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

        TextView motivo, nombreUsuario, fechaI, fechaF, descripcion , estado;
        Button aceptar, denegar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            motivo = itemView.findViewById(R.id.tvMotivoAusenciaDato);
            nombreUsuario = itemView.findViewById(R.id.tvNombreUsuarioAusenciaDato);
            fechaI = itemView.findViewById(R.id.tvFechaInicioAusencia);
            fechaF = itemView.findViewById(R.id.tvFechaFinAusencia);
            descripcion = itemView.findViewById(R.id.tvDescripcionAusenciaDato);
            estado = itemView.findViewById(R.id.tvEstadoAusencia);
            aceptar = itemView.findViewById(R.id.btAceptarAusencia);
            denegar = itemView.findViewById(R.id.btDenegarAusencia);
        }
    }
}
