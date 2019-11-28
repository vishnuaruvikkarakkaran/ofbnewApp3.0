package com.enfin.ofabee3.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.enfin.ofabee3.utils.Constants.KEY_ACCESS_TOKEN;
import static com.enfin.ofabee3.utils.Constants.DATABASE_NAME;
import static com.enfin.ofabee3.utils.Constants.DATABASE_VERSION;
import static com.enfin.ofabee3.utils.Constants.KEY_CATEGORY_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_DEVICE_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_EMAIL_VERIFIED;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_EMAIL;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_ID;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_IMAGE;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_NAME;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_PHONE_NUMBER;
import static com.enfin.ofabee3.utils.Constants.KEY_USER_STATUS;
import static com.enfin.ofabee3.utils.Constants.TABLE_USER;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method for creating table.
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " TEXT," + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT," + KEY_USER_IMAGE + " TEXT,"
                + KEY_USER_PHONE_NUMBER + " TEXT," + KEY_USER_STATUS + " TEXT,"
                + KEY_DEVICE_ID + " TEXT," + KEY_EMAIL_VERIFIED + " TEXT,"
                + KEY_CATEGORY_ID + " TEXT," + KEY_ACCESS_TOKEN + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);

    }

}
