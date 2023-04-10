package com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pedroaguilar.andarivel.R;
import com.pedroaguilar.andarivel.modelo.Anuncio;
import com.pedroaguilar.andarivel.servicios.ServicioFirebaseStorage;

import java.io.File;
import java.util.ArrayList;

/**
 * Proyect: Andarivel
 * From: com.pedroaguilar.andarivel.presentacion.ui.panelAdministrador.tablonAnuncios.empleados
 * Create by Pedro Aguilar Fernández on 15/12/2022 at 19:09
 * More info: linkedin.com/in/pedro-aguilar-fernández-167753140
 * All rights reserved 2022
 **/
public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.AnuncioViewHolder> {

    private ArrayList<Anuncio> listaAnuncios;
    private final ServicioFirebaseStorage storage = new ServicioFirebaseStorage();
    private final TablonAnunciosEmplPresenter presenter = new TablonAnunciosEmplPresenter();

    public AnuncioAdapter(ArrayList<Anuncio> lista) {
        this.listaAnuncios = lista;
    }

    @NonNull
    @Override
    public AnuncioAdapter.AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignar el diseño de los elemetos de la lista al recurso vista e departamentos layout
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.anuncio_item, parent, false);
        return new AnuncioAdapter.AnuncioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioAdapter.AnuncioViewHolder holder, int position) {
        // asiganacion de los elementos del componente, antes tienen que estar dados de alta como
        // elementos de UsuarioViewHolder de abajo, son los metodos de tu entidad Usuario
        Context context = holder.itemView.getContext();
        Anuncio anuncio = listaAnuncios.get(holder.getBindingAdapterPosition());
        holder.title.setText(listaAnuncios.get(position).getTitle());
        holder.descripcion.setText(listaAnuncios.get(position).getDescripcion());
        if (!listaAnuncios.get(position).getImgUrl().equals("")) {
            holder.imgUrl.setOnClickListener(v -> {
                presenter.verImgAnuncio(createTempFile(v.getContext(), anuncio), anuncio, task -> {
                    if (task.isSuccessful()) {
                        viewDoc(presenter.localDoc, v.getContext());
                    } else {
                        Toast.makeText(context, "Error al descargar el adjunto", Toast.LENGTH_LONG).show();
                    }
                });
            });
            //cargar imagen
            Glide.with(context)
                    //                .load(anuncio.getImgUrl())
                    .load(storage.getAnuncioUrl(anuncio.getId()))
                    //este es para que almacene la imagen descargada, para que no tenga que estar
                    // consultando cada vez que inicie la app. Tiene la desventaja que hasta que no cambie
                    // la url, la imagen va a ser la misma sin importar que el servidor si cambie
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //poner este icono en lugar de la imagen para que el usuario sepa que la imagen esta
                    // cargando
                    .placeholder(R.drawable.ic_access_time)
                    //poner este icono en lugar de la imagen para que el usuario sepa que la imagen contiene
                    // algun error
                    .error(R.drawable.ic_broken_image)
                    .centerCrop()
                    .into(holder.imgUrl);
        } else {
            holder.imgUrl.setVisibility(View.INVISIBLE);
        }
        if (!listaAnuncios.get(position).getLink().equals("")) {
            holder.link.setText(listaAnuncios.get(position).getLink());
        } else {
            holder.link.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        //Necesita saber el tamaño de la lista
        return listaAnuncios.size();
    }

    public static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        TextView title, descripcion, link;
        ImageButton imgUrl;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitleAnuncio);
            descripcion = itemView.findViewById(R.id.tvDescriptionAnuncio);
            link = itemView.findViewById(R.id.tvLinkItem);
            imgUrl = itemView.findViewById(R.id.imgAnuncio);
        }
    }

    private File createTempFile(Context context, Anuncio anuncio) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = new File(file.getAbsolutePath() + "/ImagenesDeAndarivel");
        dir.mkdirs();
        return new File(dir, anuncio.getId());
    }

    private void viewDoc(File file, Context context) {
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}