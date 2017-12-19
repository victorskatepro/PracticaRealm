package com.victorsaico.practicarealm.activities.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.adapters.PublicacionesAdapter;
import com.victorsaico.practicarealm.activities.models.Publicacion;
import com.victorsaico.practicarealm.activities.models.Usuario;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private RecyclerView publicacioneslist;
    private Realm realm = Realm.getDefaultInstance();
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmResults<Publicacion> pubs = realm.where(Publicacion.class).findAll();

        insertarData();

        Log.d(TAG,"pubs:"+pubs);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        publicacioneslist = (RecyclerView) findViewById(R.id.recyclerview);

        publicacioneslist.setLayoutManager(new LinearLayoutManager(this));

        publicacioneslist.setAdapter(new PublicacionesAdapter(this));

        PublicacionesAdapter adapter = (PublicacionesAdapter)publicacioneslist.getAdapter();
        adapter.setPublicaciones(pubs);

    }
    public void goLogout(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            boolean success = editor.putBoolean("islogged", false).commit();
            finish();
    }
    public void insertarData(){
        final Publicacion p = new Publicacion(0,"publicacion1","jun.29 a las 10:30 pm",R.drawable.imagen1);
        final Publicacion p2 = new Publicacion(1, "publicacion2", "jun.29 a las 10:30 pm",R.drawable.imagen2);
        final Publicacion p3 = new Publicacion(2,"Publicacion3", "jun.29 a las 10:30 pm", R.drawable.imagen3);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Usuario user = realm.where(Usuario.class).findFirst();
                user.getPublicacions().add(p);
                user.getPublicacions().add(p2);
                user.getPublicacions().add(p3);

                Log.d(TAG,"publicaciones: "+user.getPublicacions());
            }
        });
    }
}
