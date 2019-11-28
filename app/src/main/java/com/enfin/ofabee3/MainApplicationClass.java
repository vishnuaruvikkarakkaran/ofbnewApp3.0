package com.enfin.ofabee3;

import android.app.Application;
import android.content.ContextWrapper;

import com.enfin.ofabee3.utils.MediaLoader;
import com.enfin.ofabee3.utils.OBLogger;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.pixplicity.easyprefs.library.Prefs;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class MainApplicationClass extends Application {

    private static MainApplicationClass instance = null;

    public static MainApplicationClass get() {
        if (instance == null)
            return new MainApplicationClass();
        else
            return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Stetho.initializeWithDefaults(this);
        OBLogger.init();
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader()).build());
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        /*//crash handler
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(false) //default: true
                .showErrorDetails(false) //default: true
                .showRestartButton(false) //default: true
                .logErrorOnRestart(false) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.ic_logo) //default: bug image
                //.restartActivity(YourCustomActivity.class) //default: null (your app's launch activity)
                //.errorActivity(YourCustomErrorActivity.class) //default: null (default error activity)
                //.eventListener(new YourCustomEventListener()) //default: null
                .apply();*/
    }
}
