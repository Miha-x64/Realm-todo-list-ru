package net.aquadc.realmtest;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmTestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Realm'у нужен конфиг, пусть даже пустой
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(this).build());
    }
}
