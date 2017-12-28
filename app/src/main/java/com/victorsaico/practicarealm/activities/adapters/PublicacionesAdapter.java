package com.victorsaico.practicarealm.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.activities.MainActivity;
import com.victorsaico.practicarealm.activities.models.Publicacion;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by JARVIS on 19/12/2017.
 */

public class PublicacionesAdapter extends RecyclerView.Adapter<PublicacionesAdapter.ViewHolder> {
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Publicacion> publicaciones ;
    private Realm realm = Realm.getDefaultInstance();
    private Activity activity;

    public PublicacionesAdapter(Activity activity)
    {
        this.publicaciones = new ArrayList<>();
        this.activity = activity;
    }

    public void setPublicaciones(List<Publicacion> pubs) {
        this.publicaciones = pubs;
    }

    public void setFilter(ArrayList<Publicacion> publicacionesFilter)
    {
        Log.d(TAG,"listafiltrada"+ publicacionesFilter);
        publicaciones = new ArrayList<>();
        publicaciones.addAll(publicacionesFilter);
        notifyDataSetChanged();
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
        Bitmap bitmap2 = null;
        //convertimos los string a uri para poder obtener la referencia de la imagen
        try {
            Uri uri = Uri.parse(user.getImagenprofile());
            bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);
            viewholder.imagenautor.setImageBitmap(bitmap);
            Uri uri2 = Uri.parse(publicacion.getImagen());
            bitmap2 = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri2);
            viewholder.imagenpub.setImageBitmap(bitmap2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String time = getDate(publicacion.getFecha());
        Log.d(TAG,"publicacion:"+publicacion);
        viewholder.titulopub.setText(publicacion.getTitulo());
        viewholder.fechapub.setText(time);
        viewholder.autorpub.setText(user.getNombre());
       // viewholder.imagenautor.setImageByte;
    }

    public String getDate(Date date)
    {
        String time = null;
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("MMM dd");
            SimpleDateFormat hora = new SimpleDateFormat("hh:mm");
            SimpleDateFormat type = new SimpleDateFormat("a");
            Log.d(TAG, fecha.format(date) + "a las " + hora.format(date));

            String hor = type.format(date);
            String fechaformat = String.valueOf(fecha.format(date));
            String horaformat = String.valueOf(hora.format(date));

            String h = hor.substring(0, 1);
            String o = hor.substring(3, 4);
            String marker = h + o;
            time = fechaformat + " a las " + horaformat + marker;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return time;
    }
    @Override
    public int getItemCount()
    {
        return this.publicaciones.size();
    }
}
