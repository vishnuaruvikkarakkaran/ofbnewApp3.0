package com.enfin.ofabee3.data.local.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.enfin.ofabee3.data.remote.model.Response.LoginResponseModel;
import com.enfin.ofabee3.di.ApplicationContext;
import com.enfin.ofabee3.utils.OBLogger;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.enfin.ofabee3.utils.Constants.KEY_ACCESS_TOKEN;
import static com.enfin.ofabee3.utils.Constants.KEY_EMAIL_VERIFIED;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_EMAIL;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_IMAGE;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_NAME;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_STATUS;
import static com.enfin.ofabee3.utils.Constants.TABLE_USER;

/**
 * Created by SARATH on 21/8/19.
 */

@Singleton
public class OBDBHelper implements DBHelper {

    SQLiteDatabase database;

    @Inject
    public OBDBHelper(@ApplicationContext Context context) {
        database = new DatabaseHandler(context).getWritableDatabase();
    }

    @Override
    public boolean insertUserData(LoginResponseModel.DataBean userModel) {
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, userModel.getUser().getId());
            values.put(KEY_USER_NAME, userModel.getUser().getUs_name());
            values.put(KEY_USER_EMAIL, userModel.getUser().getUs_email());
            values.put(KEY_USER_PHONE_NUMBER, userModel.getUser().getUs_phone());
            values.put(KEY_USER_IMAGE, userModel.getUser().getUs_image());
            values.put(KEY_USER_STATUS, userModel.getUser().getUs_status());
            //values-mdpi.put(KEY_DEVICE_ID,userModel.);
            values.put(KEY_EMAIL_VERIFIED, userModel.getUser().getUs_email_verified());
            //values-mdpi.put(KEY_CATEGORY_ID,userModel.getUs_category_id().toString());
            values.put(KEY_ACCESS_TOKEN, userModel.getToken());
            database.insert(TABLE_USER, null, values);
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            OBLogger.e("Insertion Error : ", e.getMessage());
            close();
            return false;
        }
    }

    @Override
    public boolean isUserLoggedIn() {
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_USER_ID;
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex(KEY_USER_ID)) != null) {
                    cursor.close();
                    close();
                    return true;
                } else {
                    //database.execSQL("delete from " + TABLE_USER);
                    cursor.close();
                    close();
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public String getAccessToken() {
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            String token = cursor.getString(cursor.getColumnIndex(KEY_ACCESS_TOKEN));
            cursor.close();
            close();
            return token;
        } else {
            close();
            return null;
        }
    }

    @Override
    public String getUserID() {
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String token = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
            cursor.close();
            close();
            return token;
        } else {
            close();
            return null;
        }
    }

    @Override
    public LoginResponseModel.DataBean.UserBean getUserDetails() {
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        Cursor cursor = database.rawQuery(selectQuery, null);
        LoginResponseModel.DataBean.UserBean userBean = new LoginResponseModel.DataBean.UserBean();
        if (cursor != null) {
            cursor.moveToFirst();
            userBean.setId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
            userBean.setUs_name(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            userBean.setUs_email(cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));
            userBean.setUs_image(cursor.getString(cursor.getColumnIndex(KEY_USER_IMAGE)));
            userBean.setUs_phone(cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE_NUMBER)));
            cursor.close();
            close();
            return userBean;
        } else {
            close();
            return null;
        }
    }

    @Override
    public void deleteUserData() {
        //database.execSQL("delete from " + TABLE_USER);
        database.delete(TABLE_USER, null, null);
        close();
    }

    @Override
    public void updateUserDetails(LoginResponseModel.DataBean.UserBean user) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_NAME, user.getUs_name());
        cv.put(KEY_USER_EMAIL, user.getUs_email());
        cv.put(KEY_USER_PHONE_NUMBER, user.getUs_phone());
        cv.put(KEY_USER_IMAGE, user.getUs_image());
        database.update(TABLE_USER, cv, KEY_USER_ID + "=" + user.getId(), null);
        close();
    }

    @Override
    public void updateUserAvatar(String userId, String userImage) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_IMAGE, userImage);
        database.update(TABLE_USER, cv, KEY_USER_ID + "=" + userId, null);
        close();
    }

    @Override
    public synchronized void close () {
        if (database != null) {
            database.close();
        }
    }
}
