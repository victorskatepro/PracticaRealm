package com.victorsaico.practicarealm.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.activities.EditarPerfilActivity;
import com.victorsaico.practicarealm.activities.activities.LoginActivity;
import com.victorsaico.practicarealm.activities.activities.MainActivity;
import com.victorsaico.practicarealm.activities.adapters.PublicacionesAdapter;
import com.victorsaico.practicarealm.activities.models.Publicacion;
import com.victorsaico.practicarealm.activities.models.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class ProfileFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView publicacioneslist;

    private SharedPreferences sharedPreferences;
    RealmResults<Publicacion> pubs;

    private Realm realm = Realm.getDefaultInstance();
    private Toolbar toolbar;
    ArrayList<Publicacion> publicacions;
    private PublicacionesAdapter adapter;
    private TextView txtnombreperfil,empresaperfil,correoperfil,telefonoperfil;
    private ImageView imageprofile;
    private int idusuario;
    private  Usuario usuario;

    public ProfileFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        txtnombreperfil = view.findViewById(R.id.txtnombreprofile);
        empresaperfil = view.findViewById(R.id.txtempresaprofile);
        correoperfil = view.findViewById(R.id.txtcorreoprofile);
        telefonoperfil = view.findViewById(R.id.txtfechaprofile);

        imageprofile = view.findViewById(R.id.imgfgprofile);
        Log.d(TAG,"ENTRANDO A RECOGER DATOS");

        getProfilewrite();

        publicacions = new ArrayList<>();

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        toolbar = (Toolbar) view.findViewById(R.id.profile_toolbar);
        activity.setSupportActionBar(toolbar);


        Log.d(TAG,"ENTRANDO A RECOGER DATOS");

        pubs = realm.where(Publicacion.class).findAllSorted("id", Sort.ASCENDING);
        Log.d(TAG,"TOTAL"+pubs.size());
       // pubsusuario = realm.where(Usuario.class).equalTo("publicacions.id", idusuario).findFirst();

        publicacioneslist = (RecyclerView) view.findViewById(R.id.recyclerviewperfil);

        publicacioneslist.setLayoutManager(new LinearLayoutManager(getActivity()));

        publicacioneslist.setAdapter(new PublicacionesAdapter(getActivity()));
//
       adapter = (PublicacionesAdapter)publicacioneslist.getAdapter();

        RealmResults<Usuario> user = realm.where(Usuario.class)
                .equalTo("id",idusuario).findAll();

        RealmList<Publicacion> filteredModelList = new RealmList<>();
        for(Usuario userpub : user)
        {
            for(Publicacion pubs2 : userpub.getPublicacions())
            {
                filteredModelList.add(pubs2);
            }

        }

        List<Publicacion> arrayListOfUnmanagedObjects = realm.copyFromRealm(filteredModelList);

        adapter.setPublicaciones(arrayListOfUnmanagedObjects);

        return view;
    }
    public List<Publicacion> filtrarList(List<Publicacion> pubgenerales)
    {
        Log.d(TAG,"pubsgenerales"+pubgenerales.size());
        List<Publicacion> listFiltrada = new ArrayList<>();
        listFiltrada.clear();

        try {
            for (int i = 0; i<=pubgenerales.size(); i++) {
                for (Publicacion pubs22 : pubgenerales) {

                    Log.d(TAG, "contador" + i);
                    Usuario user = realm.where(Usuario.class)
                            .equalTo("id",idusuario).equalTo("publicacions.id", pubs22.getId()).findFirst();

                    Log.d(TAG, "userpubs" + idusuario);

                    Log.d(TAG, "publicaciones filtradas" + idusuario + user.getPublicacions());

                    int idpublicacion = user.getPublicacions().get(i).getId();

                    Log.d(TAG,"publicacionid"+idpublicacion);
                }
            }
            //List<Publicacion> arrayListOfUnmanagedObjects = realm.copyFromRealm(listFiltrada);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d(TAG,"publicacionesfiltradas"+listFiltrada);
        return listFiltrada;
    }
    public void getProfilewrite()
    {
        Context applicationContext = getActivity().getApplicationContext();
        Bitmap bitmap = null;
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String nombre = sharedPreferences.getString("nombre", null);
            String correo = sharedPreferences.getString("correo", null);
            String imagenprofile = sharedPreferences.getString("imageprofile", null);
            idusuario = sharedPreferences.getInt("id", -1);
            String empresa = sharedPreferences.getString("empresa", null);
            String telefono = sharedPreferences.getString("telefono", null);
            txtnombreperfil.setText(nombre);
            correoperfil.setText(correo);
            empresaperfil.setText(empresa);
            telefonoperfil.setText(telefono);
            Log.d(TAG,"imagenperfil"+imagenprofile+nombre);
            Uri uri = Uri.parse(imagenprofile);
            bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), uri);
           imageprofile.setImageBitmap(bitmap);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mainprofile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itm_logout:
                goLogout();
                break;
            case R.id.itm_editar:
                goEditProfile();
        }
        return super.onOptionsItemSelected(item);
    }
    public void goEditProfile()
    {
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    //metodo para salir del menu principal
    public void goLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor.putBoolean("islogged", false).commit();
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
