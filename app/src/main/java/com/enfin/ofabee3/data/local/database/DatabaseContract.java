package com.enfin.ofabee3.data.local.database;

import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;

public interface DatabaseContract {

    interface Iview{

        void isSuccessfullyInserted(boolean status);
        void onDatabaseSuccessfullyDeleted();

    }

    interface Ipresenter{
        void insertUserData(LoginResponseModel.DataBean userModel);
        void deleteDatabaseValues();
        String getItemFromDatabase(String key);
        boolean isUserLoggedIn();
    }

}
