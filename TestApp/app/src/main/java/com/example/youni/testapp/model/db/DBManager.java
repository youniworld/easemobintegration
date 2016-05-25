package com.example.youni.testapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.youni.testapp.model.DemoUser;
import com.example.youni.testapp.model.InvitationInfo;
import com.example.youni.testapp.model.InvitationInfo.InvitationStatus;

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

    public boolean saveContacts(Collection<DemoUser> contacts){
        if(contacts== null ||contacts.isEmpty()){
            return false;
        }

        checkAvailability();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();

        for(DemoUser user:contacts){
            ContentValues values = new ContentValues();

            values.put(UserTable.COL_USERNAME,user.getUserName());
            values.put(UserTable.COL_HXID,user.getHxId());
            values.put(UserTable.COL_AVATAR,user.getAvatarPhoto());

            db.replace(UserTable.TABLE_NAME,null,values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
    }

    public List<DemoUser> getContacts(){
        checkAvailability();

        List<DemoUser> users = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from " + UserTable.TABLE_NAME,null);

        while(cursor.moveToNext()){
            DemoUser user = new DemoUser();
            user.setUserName(cursor.getString(cursor.getColumnIndex(UserTable.COL_USERNAME)));
            user.setHxId(cursor.getString(cursor.getColumnIndex(UserTable.COL_HXID)));
            user.setAvatarPhoto(cursor.getString(cursor.getColumnIndex(UserTable.COL_AVATAR)));

            users.add(user);
        }

        cursor.close();

        return users;
    }

    public void saveContact(DemoUser user){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserTable.COL_HXID,user.getHxId());
        values.put(UserTable.COL_USERNAME,user.getUserName());
        values.put(UserTable.COL_AVATAR,user.getAvatarPhoto());

        db.replace(UserTable.TABLE_NAME, null, values);
    }

    public void deleteContact(DemoUser user){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(UserTable.TABLE_NAME, UserTable.COL_HXID + " = ? ", new String[]{user.getHxId()});
    }

    public List<InvitationInfo> getContactInvitations(){

        List<InvitationInfo> inviteInfos = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + InvitationMessageTable.TABLE_NAME,null);

        while(cursor.moveToNext()){
            DemoUser user = new DemoUser();
            user.setHxId(cursor.getString(cursor.getColumnIndex(InvitationMessageTable.COL_HXID)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(InvitationMessageTable.COL_USERNAME)));

            InvitationInfo info = new InvitationInfo();
            info.setUser(user);

            info.setStatus(intToInviteStatus(cursor.getInt(cursor.getColumnIndex(InvitationMessageTable.COL_INVITE_STATUS))));
            info.setReason(cursor.getString(cursor.getColumnIndex(InvitationMessageTable.COL_REASON)));
            inviteInfos.add(info);
        }

        return inviteInfos;
    }

    private InvitationStatus intToInviteStatus(int intStatus){
        if(intStatus == InvitationStatus.NEW_INVITE.ordinal()){
            return InvitationStatus.NEW_INVITE;
        }

        if(intStatus == InvitationStatus.INVITE_ACCEPT.ordinal()){
            return InvitationStatus.INVITE_ACCEPT;
        }

        return InvitationStatus.INVITE_ACCEPT_BY_PEER;
    }

    public void addInvitation(InvitationInfo invitationInfo){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InvitationMessageTable.COL_HXID,invitationInfo.getUser().getHxId());
        values.put(InvitationMessageTable.COL_INVITE_STATUS,invitationInfo.getStatus().ordinal());
        values.put(InvitationMessageTable.COL_REASON,invitationInfo.getReason());
        values.put(InvitationMessageTable.COL_USERNAME,invitationInfo.getUser().getUserName());

        db.replace(InvitationMessageTable.TABLE_NAME,null,values);
    }

    public void removeInvitation(String hxId){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(InvitationMessageTable.TABLE_NAME, InvitationMessageTable.COL_HXID + " =? ", new String[]{hxId});
    }

    public void updateInvitationStatus(InvitationStatus invitationStatus,String hxId){
        ContentValues values = new ContentValues();
        values.put(InvitationMessageTable.COL_INVITE_STATUS, invitationStatus.ordinal());

        updateInvitationInfo(values, hxId);
    }

    public void updateInvitationUserName(String username,String hxId){
        ContentValues values = new ContentValues();
        values.put(InvitationMessageTable.COL_USERNAME,username);

        updateInvitationInfo(values,hxId);
    }

    private void updateInvitationInfo(ContentValues updateValues, String hxId){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.update(InvitationMessageTable.TABLE_NAME,updateValues, InvitationMessageTable.COL_HXID + "=?",new String[]{hxId});
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
            db.execSQL(InvitationMessageTable.SQL_CREATE_TABLE);
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

class InvitationMessageTable {
    static final String TABLE_NAME = "invitation_message";
    static final String COL_HXID = "_hx_id";
    static final String COL_USERNAME = "_username";
    static final String COL_REASON ="_reason";
    static final String COL_INVITE_STATUS ="_invite_status";

    static final String SQL_CREATE_TABLE = "CREATE TABLE "
                                            + TABLE_NAME + " ("
                                            + COL_INVITE_STATUS + "I NTEGER , "
                                            + COL_REASON + " TEXT, "
                                            + COL_USERNAME + " TEXT, "
                                            + COL_HXID + " TEXT PRIMARY KEY);";
}