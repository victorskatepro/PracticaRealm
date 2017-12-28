package com.victorsaico.practicarealm.activities.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.victorsaico.practicarealm.R;
import com.victorsaico.practicarealm.activities.fragments.CalendarFragment;
import com.victorsaico.practicarealm.activities.fragments.HomeFragment;
import com.victorsaico.practicarealm.activities.fragments.MessagesFragment;
import com.victorsaico.practicarealm.activities.fragments.ProfileFragment;
import com.victorsaico.practicarealm.activities.fragments.RegisterFragment;
import com.victorsaico.practicarealm.activities.models.Publicacion;

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
        final Fragment homeFragment = new HomeFragment();
        final Fragment calendarFragment = new CalendarFragment();
        final Fragment messageFragment = new MessagesFragment();
        final Fragment profileFragment = new ProfileFragment();
        final Fragment registerFragment = new RegisterFragment();

        RealmResults<Publicacion> pubs = realm.where(Publicacion.class).findAll();

       // insertarData();

        Log.d(TAG,"pubs:"+pubs);

        //Configuracion del bottomnavigation
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
         //Inicializar el primer fragment
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, homeFragment).commit();
        }
        //funcion para cambiar entre fragments cuando se precionan
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(item.getItemId() == R.id.itmmenu)
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, homeFragment ).commit();
                }else if (item.getItemId() == R.id.itmcalendar)
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, calendarFragment).commit();
                }else if (item.getItemId() == R.id.itmadd)
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, registerFragment).commit();
                }else if (item.getItemId() == R.id.itmcomment)
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, messageFragment).commit();
                }else if (item.getItemId() == R.id.itmperfil)
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, profileFragment).commit();
                }
                return true;
            }
        });
    }


}
