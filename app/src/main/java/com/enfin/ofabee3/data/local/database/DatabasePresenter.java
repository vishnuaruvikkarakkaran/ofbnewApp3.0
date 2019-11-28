package com.enfin.ofabee3.data.local.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.enfin.ofabee3.ui.base.mvp.BasePresenter;
import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;

import static com.enfin.ofabee3.utils.Constants.KEY_ACCESS_TOKEN;
import static com.enfin.ofabee3.utils.Constants.KEY_EMAIL_VERIFIED;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_EMAIL;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_IMAGE;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_NAME;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_STATUS;
import static com.enfin.ofabee3.utils.Constants.TABLE_USER;


public class DatabasePresenter extends BasePresenter implements DatabaseContract.Ipresenter {

    private DatabaseContract.Iview iView;
    private SQLiteDatabase database;

    public DatabasePresenter(DatabaseContract.Iview view){
        iView = view;
        database = new DatabaseHandler((Context) view).getWritableDatabase();
    }

    @Override
    public void insertUserData(LoginResponseModel.DataBean userModel) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID,userModel.getUser().getId());
            values.put(KEY_USER_NAME,userModel.getUser().getUs_name());
            values.put(KEY_USER_EMAIL,userModel.getUser().getUs_email());
            values.put(KEY_USER_PHONE_NUMBER,userModel.getUser().getUs_phone());
            values.put(KEY_USER_IMAGE,userModel.getUser().getUs_image());
            values.put(KEY_USER_STATUS,userModel.getUser().getUs_status());
            //values-mdpi.put(KEY_DEVICE_ID,userModel.);
            values.put(KEY_EMAIL_VERIFIED,userModel.getUser().getUs_email_verified());
            //values-mdpi.put(KEY_CATEGORY_ID,userModel.getUs_category_id().toString());
            values.put(KEY_ACCESS_TOKEN,userModel.getToken());
            database.insert(TABLE_USER,null,values);
            iView.isSuccessfullyInserted(true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Insertion Error : ",e.getMessage());
            iView.isSuccessfullyInserted(false);
        }
    }

    @Override
    public void deleteDatabaseValues() {
        database.execSQL("delete from "+ TABLE_USER);
        iView.onDatabaseSuccessfullyDeleted();
    }

    @Override
    public String getItemFromDatabase(String key) {
        try {
            String item;
            String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                    + key;
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor != null)
                cursor.moveToFirst();
            item = cursor.getString(cursor.getColumnIndex(key));
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isUserLoggedIn() {
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_USER_ID;
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor != null)
                cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex(KEY_USER_ID))!=null){
                return true;
            }else {
                database.execSQL("delete from "+ TABLE_USER);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
