package com.victorsaico.practicarealm.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.models.Publicacion;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by JARVIS on 19/12/2017.
 */

public class PublicacionesAdapter extends RecyclerView.Adapter<PublicacionesAdapter.ViewHolder> {

    private List<Publicacion> publicaciones ;
    private Realm realm = Realm.getDefaultInstance();
    private Activity activity;

    public PublicacionesAdapter(Activity activity)
    {
        this.publicaciones = new ArrayList<>();
        this.activity = activity;    }

    public void setPublicaciones(RealmResults<Publicacion> pubs) {
        this.publicaciones = pubs;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView titulopub;
        public TextView autorpub;
        public ImageView imagenpub;
        public TextView fechapub;
        public ImageView imagenautor;

        public ViewHolder(View itemView)
        {
            super(itemView);
            titulopub = (TextView) itemView.findViewById(R.id.txttitulo);
            autorpub = (TextView) itemView.findViewById(R.id.txtpropietario);
            fechapub = (TextView) itemView.findViewById(R.id.txtfecha);
            imagenpub = (ImageView) itemView.findViewById(R.id.imgpublicacion);
            imagenautor = (ImageView) itemView.findViewById(R.id.imagenautor);
        }
    }

    @Override
    public PublicacionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacion, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PublicacionesAdapter.ViewHolder viewholder, int position)
    {
        Publicacion publicacion = this.publicaciones.get(position);

        Usuario user = realm.where(Usuario.class)
                .equalTo("publicacions.id",publicacion.getId()).findFirst();
        Context applicationContext = activity.getApplicationContext();
        Bitmap bitmap = null;
        try {
            Uri uri = Uri.parse(user.getImagenprofile());
            bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);
            viewholder.imagenautor.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewholder.titulopub.setText(publicacion.getTitulo());
        viewholder.fechapub.setText(publicacion.getFecha());
        viewholder.imagenpub.setImageResource(publicacion.getImagen());
        viewholder.autorpub.setText(user.getNombre());
       // viewholder.imagenautor.setImageByte;
    }

    @Override
    public int getItemCount()
    {
        return this.publicaciones.size();
    }
}
