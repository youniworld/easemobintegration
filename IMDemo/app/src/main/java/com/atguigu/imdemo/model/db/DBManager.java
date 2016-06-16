package com.atguigu.imdemo.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;

import com.atguigu.imdemo.model.IMUser;
import com.atguigu.imdemo.model.InvitationInfo;

import java.util.ArrayList;
import java.util.List;

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

    public List<InvitationInfo> getInvitationList(){
        List<InvitationInfo> invitationInfos = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + InvitationTable.TABLE_NAME, null);

        while (cursor.moveToNext()){
            InvitationInfo info = getInvitation(cursor);

            invitationInfos.add(info);
        }

        cursor.close();

        return invitationInfos;
    }

    public void updateInvitationInfo(InvitationInfo invitationInfo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InvitationTable.COL_APPUSER,invitationInfo.getAppUser().getAppUser());
        values.put(InvitationTable.COL_HXID,invitationInfo.getAppUser().getHxId());
        values.put(InvitationTable.COL_AVARTAR,invitationInfo.getAppUser().getAvartar());
        values.put(InvitationTable.COL_NICK,invitationInfo.getAppUser().getNick());
        values.put(InvitationTable.COL_INVITE_STATE,invitationInfo.getInviateState().ordinal());
        values.put(InvitationTable.COL_REASON,invitationInfo.getReason());

        db.replace(InvitationTable.TABLE_NAME, null, values);
    }

    public InvitationInfo getInvitationInfoByHXID(String hxId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + InvitationTable.TABLE_NAME + " where " + InvitationTable.COL_HXID + " =?", new String[]{hxId});

        if(cursor.moveToNext()){
            InvitationInfo info = getInvitation(cursor);
            cursor.close();
            return info;
        }

        return null;
    }

    private InvitationInfo getInvitation(Cursor cursor){
        IMUser user = new IMUser();
        InvitationInfo info = new InvitationInfo(user);

        user.setAppUser(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_APPUSER)));
        user.setHxId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_HXID)));
        user.setNick(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_NICK)));
        user.setAvartar(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_AVARTAR)));

        info.setInviateState(int2InviteState(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_INVITE_STATE))));
        info.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));

        return info;
    }
    private InvitationInfo.InviateState int2InviteState(int state){
        if(InvitationInfo.InviateState.INVITE_NEW.ordinal() == state){
            return InvitationInfo.InviateState.INVITE_NEW;
        }else if(InvitationInfo.InviateState.INVITE_ACCEPT.ordinal() == state){
            return InvitationInfo.InviateState.INVITE_ACCEPT;
        }else{
            return InvitationInfo.InviateState.INVITE_ACCEPT_BY_PEER;
        }
    }

    static class DBHelper extends SQLiteOpenHelper{

        DBHelper(Context context, String name){
            super(context,name,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(UserTable.CREATE_TABLE);
            db.execSQL(InvitationTable.CREATE_TABLE);
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
                                        + COL_AVARTAR + " TEXuniT);";
}

class InvitationTable{
    static final String TABLE_NAME = "_invite";
    static final String COL_APPUSER = "_appuser";
    static final String COL_NICK = "_nick";
    static final String COL_AVARTAR = "_avartar";
    static final String COL_HXID = "_hxid";
    static final String COL_INVITE_STATE = "_invite_state";
    static final String COL_REASON = "_reason";

    static final String CREATE_TABLE = "CREATE TABLE "
                                        + TABLE_NAME + " ("
                                        + COL_APPUSER + " TEXT PRIMARY KEY, "
                                        + COL_HXID + " TEXT, "
                                        + COL_NICK + " TEXT, "
                                        + COL_AVARTAR + " TEXT, "
                                        + COL_INVITE_STATE + " INTEGER,"
                                        + COL_REASON + " TEXT);";
}