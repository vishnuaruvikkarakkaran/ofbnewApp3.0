package com.enfin.ofabee3.data.local.sharedpreferences;

/**
 * Created by SARATH on 21/8/19.
 */
public interface PreferencesHelper {

    boolean getCurrentUserLoggedInStatus();

    void setCurrentUserLoggedInStatus(boolean status);

    String getCurrentUserCountry();

    void setCurrentUserCountry(String country);
}
