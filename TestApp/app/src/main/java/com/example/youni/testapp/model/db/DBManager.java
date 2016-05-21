package com.example.youni.testapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.youni.testapp.model.Model;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by youni on 2016/5/19.
 */
public class DBManager {
    private static final int DB_VERSION = 1;
    private static DBManager me = null;

    private String mDBName;
    private Context mContext;
    private DBHelper mHelper;

    public DBManager(Context context, String dbName){
        init(context,dbName);
    }

    private void init(Context context, String dbName){
        mContext = context;
        mDBName = dbName;

        mHelper = new DBHelper(mContext,mDBName);
    }

    public boolean saveContacts(Collection<Model.DemoUser> contacts){
        if(contacts== null ||contacts.isEmpty()){
            return false;
        }

        checkAvailability();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();

        for(Model.DemoUser user:contacts){
            ContentValues values = new ContentValues();

            values.put(UserTable.COL_USERNAME,user.userName);
            values.put(UserTable.COL_HXID,user.hxId);
            values.put(UserTable.COL_AVATAR,user.avatarPhoto);

            db.replace(UserTable.TABLE_NAME,null,values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }

    public List<Model.DemoUser> getContacts(){
        checkAvailability();

        List<Model.DemoUser> users = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from " + UserTable.TABLE_NAME,null);

        while(cursor.moveToNext()){
            Model.DemoUser user = new Model.DemoUser();
            user.userName = cursor.getString(cursor.getColumnIndex(UserTable.COL_USERNAME));
            user.hxId = cursor.getString(cursor.getColumnIndex(UserTable.COL_HXID));
            user.avatarPhoto = cursor.getString(cursor.getColumnIndex(UserTable.COL_AVATAR));

            users.add(user);
        }

        cursor.close();

        return users;
    }

    private void checkAvailability(){
        if(mHelper == null){
            throw new RuntimeException("the helper is null, please init the db mananger");
        }
    }

    class DBHelper extends SQLiteOpenHelper{


        public DBHelper(Context context,String name) {
            super(context, name, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(UserTable.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

class UserTable{
    static final String TABLE_NAME = "user";

    static final String COL_USERNAME = "user_name";
    static final String COL_HXID = "hx_id";
    static final String COL_AVATAR="user_avatar";

    static final String SQL_CREATE_TABLE = "CREATE TABLE "
                                            + TABLE_NAME + "("
                                            + COL_USERNAME + " TEXT, "
                                            + COL_HXID + " TEXT, "
                                            + COL_AVATAR + " TEXT PRIMARY KEY);";

}