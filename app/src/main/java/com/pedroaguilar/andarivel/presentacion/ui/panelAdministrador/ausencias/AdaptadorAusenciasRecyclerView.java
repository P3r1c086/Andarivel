package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.ausencias;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Ausencia;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorAusenciasRecyclerView extends RecyclerView.Adapter<AdaptadorAusenciasRecyclerView.UsuarioViewHolder>{

    private final ArrayList<Ausencia> listaAusencia;
    private final ServicioFirebaseDatabase database = new ServicioFirebaseDatabase();
    private final ConcederAusenciaPresenter presenter = new ConcederAusenciaPresenter();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Context context;

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
    public void onBindViewHolder(@NonNull AdaptadorAusenciasRecyclerView.UsuarioViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // asiganacion de los elementos del componente antes tienen que estar dados de alta como elementos de UsuarioViewHolder de abajo
        //son los metodos de la entidad Usuario
        Context context = holder.itemView.getContext();
        holder.motivo.setText(" " + listaAusencia.get(position).getMotivoAusencia());
        holder.nombreUsuario.setText(" " + listaAusencia.get(position).getNombreUsuario());
        holder.fechaI.setText(listaAusencia.get(position).getFechaInicioAusencia());
        holder.fechaF.setText(listaAusencia.get(position).getFechaFinAusencia());
        holder.descripcion.setText(" " + listaAusencia.get(position).getDescripcionAusencia());
        String estado = listaAusencia.get(position).getEstado();
        holder.estado.setText(estado);
        //Escondemos o visibilizamos los botones en funcion de si el estado de la solicitud es pendiente.
        if (!estado.equals("Pendiente")) {
            holder.aceptar.setVisibility(View.INVISIBLE);
            holder.denegar.setVisibility(View.INVISIBLE);
        } else {
            holder.aceptar.setVisibility(View.VISIBLE);
            holder.denegar.setVisibility(View.VISIBLE);
        }
        ////////////TODO: NO SE SI ESTO ESTA BIEN AQUI, no me reconoce el listener
        if (listaAusencia.get(position).getAdjunto() != null) {
            holder.adjuntar.setVisibility(View.VISIBLE);
            holder.adjuntar.setOnClickListener(v -> {
                presenter.onClickBotonAdjunto(createTempFile(), taskSnapshot -> {
                    viewDoc(presenter.localDoc);
                });
            });
        } else {
            holder.adjuntar.setVisibility(View.GONE);
        }
        /////////////
        holder.aceptar.setOnClickListener(v -> {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + listaAusencia.get(position).getIdAusencia() + "/estado", "Aceptada");
            database.actualizarAusencia(childUpdates, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        listaAusencia.get(position).setEstado("Aceptada");
                        //Notificamos que uno de los item ha cambiado
                        notifyItemChanged(position);
                        Toast.makeText(context, "Ausencia Aceptada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        holder.denegar.setOnClickListener(v -> {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + listaAusencia.get(position).getIdAusencia() + "/estado", "Denegada");
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
        });
        holder.adjuntar.setOnClickListener(v -> {
//            if (listaAusencia.get(position).getAdjunto() != null) {
//                presenter.onClickBotonAdjunto(createTempFile(), taskSnapshot -> {
//                    viewDoc(presenter.localDoc);
//                });
//            }else{
//                Toast.makeText(context, "No hay documento asociado", Toast.LENGTH_SHORT).show();
//            }
        });
        holder.delete.setOnClickListener(v -> {

            database.borrarAusencia(/*position*//*getItemId(position)*/ task -> {

                //getItemId(position) me devuelve -1
                //position me devuelve la posicion en el recyclerView. 0, 1, ....
                notifyItemRemoved(position);
            });
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
        ImageView adjuntar;
        ImageButton delete;

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
            adjuntar = itemView.findViewById(R.id.imgAdjuntarDoc);
            delete = itemView.findViewById(R.id.imgDelete);
        }
    }


    private File createTempFile() {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(file.getAbsolutePath() + "/ImagenesDeAndarivel");
        dir.mkdirs();
        return new File(dir, presenter.ausenciaMostrada.getAdjunto());
    }

    private void viewDoc(File file) {
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    //todo: complemetar metodo proveniente del servicio firebase database para borrar ausencia(en nodo Ausercias y nodo Usuarios) con
    //un onLongClick().Borrar tb posible imagen del storage. Poner dialog para confirmar
}
