package com.victorsaico.practicarealm.activities.activities;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by JARVIS on 18/12/2017.
 */

public class PracticaRealm extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
