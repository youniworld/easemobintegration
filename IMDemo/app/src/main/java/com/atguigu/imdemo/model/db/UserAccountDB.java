package com.atguigu.imdemo.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.imdemo.model.IMUser;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class UserAccountDB {
    private static final int DB_VERSION = 1;
    private DBHelper dbHelper;

    public UserAccountDB(Context context){
        dbHelper = new DBHelper(context);
    }

    public void addAccount(IMUser account){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AccountTable.COL_APPUSER,account.getAppUser());
        values.put(AccountTable.COL_HXID,account.getHxId());
        values.put(AccountTable.COL_NICK,account.getNick());
        values.put(AccountTable.COL_AVARTAR,account.getAvartar());

        db.replace(AccountTable.TABLE_NAME,null,values);
    }

    public IMUser getAccount(String appUser){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + AccountTable.TABLE_NAME + " where " + AccountTable.COL_APPUSER + "=?",new String[]{appUser});

        while (cursor.moveToNext()){
            IMUser account = new IMUser(appUser);

            account.setHxId(cursor.getString(cursor.getColumnIndex(AccountTable.COL_HXID)));
            account.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_NICK)));
            account.setAvartar(cursor.getString(cursor.getColumnIndex(AccountTable.COL_AVARTAR)));
        }

        return null;
    }

    static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context,"_account",null,DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AccountTable.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    class AccountTable{
        static final String TABLE_NAME = "_account";
        static final String COL_APPUSER = "_appuser";
        static final String COL_HXID = "_hxid";
        static final String COL_NICK = "_nick";
        static final String COL_AVARTAR = "_avartar";
        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_APPUSER + " TEXT PRIMARY KEY, " + COL_HXID + " TEXT," + COL_NICK + " TEXT," + COL_AVARTAR + " TEXT);";

    }
}
