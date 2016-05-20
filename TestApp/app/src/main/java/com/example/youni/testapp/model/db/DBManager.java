package com.example.youni.testapp.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.youni.testapp.model.Model;

import java.util.ArrayList;
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

    public List<Model.DemoUser> getContacts(){

        return null;
    }

    class DBHelper extends SQLiteOpenHelper{


        public DBHelper(Context context,String name) {
            super(context, name, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public List<Model.DemoUser> getContacts(){
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from user", null);

            if(!cursor.moveToFirst()){
                cursor.close();
                return null;
            }

            List<Model.DemoUser> users = new ArrayList<>();

            do{
                Model.DemoUser user = new Model.DemoUser();
                //

                users.add(user);
            }while (cursor.moveToNext());

            return users;
        }
    }
}
