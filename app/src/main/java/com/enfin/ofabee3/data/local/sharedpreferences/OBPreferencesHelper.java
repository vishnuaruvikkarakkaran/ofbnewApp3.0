package com.enfin.ofabee3.data.local.sharedpreferences;

import android.content.Context;

import com.enfin.ofabee3.di.ApplicationContext;
import com.enfin.ofabee3.di.PreferenceInfo;
import com.enfin.ofabee3.ui.module.login.LoginActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by SARATH on 21/8/19.
 */

@Singleton
public class OBPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_STATUS";
    private static final String PREF_KEY_USER_LOCATION_COUNTRY = "PREF_KEY_USER_LOCATION_COUNTRY";

    private SharedPreferenceData preferenceData;

    @Inject
    public OBPreferencesHelper(@ApplicationContext Context context) {
        preferenceData = new SharedPreferenceData(context);
    }

    @Override
    public boolean getCurrentUserLoggedInStatus() {
        return preferenceData.getBool(PREF_KEY_USER_LOGGED_IN_MODE);
    }

    @Override
    public void setCurrentUserLoggedInStatus(boolean status) {
        preferenceData.setBool(PREF_KEY_USER_LOGGED_IN_MODE, status);
    }

    @Override
    public String getCurrentUserCountry() {
        return preferenceData.getString(PREF_KEY_USER_LOCATION_COUNTRY);
    }

    @Override
    public void setCurrentUserCountry(String country) {
        preferenceData.setString(PREF_KEY_USER_LOCATION_COUNTRY, country);
    }

}
