package com.atguigu.imdemo.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class DBManager {
    private static int DB_VERSION = 1;
    private final DBHelper dbHelper;

    public DBManager(Context context, String name){
        dbHelper = new DBHelper(context,name);
    }

    static class DBHelper extends SQLiteOpenHelper{

        DBHelper(Context context, String name){
            super(context,name,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(UserTable.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

class UserTable{
    static final String TABLE_NAME = "_user";
    static final String COL_NICK = "_nick";
    static final String COL_HXID = "_hxid";
    static final String COL_AVARTAR = "_avartar";

    static final String CREATE_TABLE = "CREATE TABLE "
                                        + TABLE_NAME + " ("
                                        + COL_NICK + " TEXT "
                                        + COL_HXID + " TEXT PRIMARY KEY "
                                        + COL_AVARTAR + " TEXT);";
}