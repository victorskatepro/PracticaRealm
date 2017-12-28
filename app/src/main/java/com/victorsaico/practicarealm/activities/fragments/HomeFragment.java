package com.victorsaico.practicarealm.activities.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.activities.LoginActivity;
import com.victorsaico.practicarealm.activities.activities.MainActivity;
import com.victorsaico.practicarealm.activities.adapters.PublicacionesAdapter;
import com.victorsaico.practicarealm.activities.models.Publicacion;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView publicacioneslist;
    private SharedPreferences sharedPreferences;
    private Realm realm = Realm.getDefaultInstance();
    private Toolbar toolbar;
    ArrayList<Publicacion> publicacionList;
    private PublicacionesAdapter adapter;
    RealmResults<Publicacion> pubs;
    public HomeFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        publicacionList = new ArrayList<>();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = (Toolbar) view.findViewById(R.id.home_toolbar);
        activity.setSupportActionBar(toolbar);
        //tienes que colocar el siguiente metodo para usar un toolbar con opciones del menú
        setHasOptionsMenu(true);
        //goLogout();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pubs = realm.where(Publicacion.class).findAllSorted("fecha", Sort.DESCENDING);

        publicacioneslist = (RecyclerView) view.findViewById(R.id.recyclerview);

        publicacioneslist.setLayoutManager(new LinearLayoutManager(getActivity()));

        publicacioneslist.setAdapter(new PublicacionesAdapter(getActivity()));

        adapter = (PublicacionesAdapter)publicacioneslist.getAdapter();

        List<Publicacion> arrayListOfUnmanagedObjects = realm.copyFromRealm(pubs);

        adapter.setPublicaciones(arrayListOfUnmanagedObjects);

        return  view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mainhome, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        adapter.setFilter(publicacionList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        Toast.makeText(getActivity(), "ASearchview Cerrado", Toast.LENGTH_SHORT).show();
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static Fragment newInstance() {
        HomeFragment home = new HomeFragment();
        return home;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //este método se convoca cada vez que se ingresa al searchview
    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Publicacion> filteredModelList = filter(pubs, newText);
        Log.d(TAG,"publicacionlist"+publicacionList);
        adapter.setFilter(filteredModelList);
        return true;
    }
    //obtiene la lista de las publicaciones y valida que contenga los parametros que se le pasen
    private ArrayList<Publicacion> filter(RealmResults<Publicacion> models, String query) {
        ArrayList<Publicacion> filteredModelList = new ArrayList<>();
        try {
            query = query.toUpperCase();
            Log.d(TAG,"modelos"+models);
            Log.d(TAG,"textofilter"+query);
            for (Publicacion model : models) {
                final String text = model.getTitulo().toUpperCase();
                if (text.contains(query)) {
                    Log.d(TAG, "textofilter" + text);
                    filteredModelList.add(model);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return filteredModelList;
    }
    public void goLogout() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor.putBoolean("islogged", false).commit();
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
