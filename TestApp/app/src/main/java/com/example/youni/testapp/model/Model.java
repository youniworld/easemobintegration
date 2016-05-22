package com.example.youni.testapp.model;

import android.content.Context;
import android.util.Log;

import com.example.youni.testapp.model.db.DBManager;
import com.example.youni.testapp.model.db.PreferenceUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youni on 2016/5/19.
 */
public class Model {
    private boolean isInited = false;
    private Context mAppContext;
    private static Model me = new Model();
    private Map<String,DemoUser> mContacts = new HashMap<>();
    private List<OnSyncListener> mContactSyncLiseners;
    private String mCurrentUser;
    private DBManager mDBManager;
    private PreferenceUtils mPreference;
    private boolean mIsContactSynced = false;

    public static class DemoUser{
        /**
         * nick 名称
         */
        public String userName;

        /**
         * 对应的环信ID
         */
        public String hxId;

        /**
         * 个人头像
         */
        public String avatarPhoto;

        /**
         * 其他个人信息
         */
        public int age;

        /**
         * education
         */
        public String education;
    }

    public static interface OnSyncListener{
        public void onSuccess();
        public void onFailed();
    }

    static public Model getInstance(){
        return me;
    }

    public boolean init(Context appContext){
        if(isInited){
            return false;
        }

        mAppContext = appContext;

        if(!EaseUI.getInstance().init(appContext, new EMOptions())){
            return false;
        }

        mContactSyncLiseners = new ArrayList<>();
        mPreference = new PreferenceUtils(mAppContext);
        mIsContactSynced = mPreference.isContactSynced();

        isInited = true;

        initListener();

        return isInited;
    }

    public void logout(final EMCallBack callBack){
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                mPreference.setContactSynced(false);
                mIsContactSynced = false;
                mContacts.clear();
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }

            @Override
            public void onProgress(int i, String s) {
                callBack.onProgress(i, s);
            }
        });
    }
    /**
     * 从远程服务器获取联系人信息
     * 1. 从环信服务器上获取
     * 2. 同时从app服务器上获取
     * 3. 等到这两个都返回时做两者的同步
     */
    public void asyncfetchUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsContactSynced = false;

                List<String> users = null;
                try {
                    users = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    mIsContactSynced = true;
                    mPreference.setContactSynced(true);
                } catch (HyphenateException e) {
                    notifyContactSyncChanged(false);
                    e.printStackTrace();

                    return;
                }

                if(users != null){
                    for(String id:users){
                        DemoUser appUser = new DemoUser();
                        appUser.hxId = id;
                        appUser.userName = null;
                        mContacts.put(id,appUser);
                    }
                }
                // fetch users from app server

                List<DemoUser> appUsers = fetchUsersFromAppServer();

                // 同步联系人
                // 以环信的联系人为主，如果环信的联系人里没有app里的联系，就把app里的联系人删除
                // 如果app里的联系人没有环信的联系人，则加入到app里

                // 最后要更新本地数据库

                mDBManager.saveContacts(mContacts.values());

                notifyContactSyncChanged(true);
            }
        }).start();
    }

    private List<DemoUser> fetchUsersFromAppServer() {
       // 实际上是应该从APP服务器上获取联系人的信息

        // 不过由于缺乏我们的demo的服务器，暂时hick下，用下假数据
        //

        for(DemoUser user:mContacts.values()){
            user.userName = user.hxId + "_guigu";
        }

        return null;
    }


    /**
     * 先加载本地的联系人
     */
    public void loadLocalContacts(){
        Log.d("Model","load local contacts");
        List<DemoUser> users = mDBManager.getContacts();

        if(users != null){
            mContacts.clear();;

            for(DemoUser user:users){
                mContacts.put(user.hxId,user);
            }
        }
    }

    public void addUser(DemoUser user){
        if(mContacts.containsKey(user.hxId)){
            return;
        }

        mContacts.put(user.hxId, user);
    }

    public void addOnContactSyncListener(OnSyncListener listener){
        if(listener == null){
            return;
        }

        if(mContactSyncLiseners.contains(listener)){
            return;
        }

        mContactSyncLiseners.add(listener);
    }

    private void notifyContactSyncChanged(boolean success){
        for(OnSyncListener listener:mContactSyncLiseners){
            if(success){
                listener.onSuccess();
            }else{
                listener.onFailed();
            }
        }
    }

    public Map<String,DemoUser> getContacts(){
        return mContacts;
    }

    public boolean isContactSynced(){
        return mIsContactSynced;
    }

    private void initListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {

            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });

        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                DemoUser user = mContacts.get(username);

                if(user != null){
                    EaseUser easeUser = new EaseUser(username);

                    easeUser.setNick(user.userName);

                    return easeUser;
                }

                return null;
            }
        });
    }

    public void onLoggedIn(String userName){
        if(mCurrentUser == userName){
            return;
        }

        mCurrentUser = userName;
        mDBManager = new DBManager(mAppContext,mCurrentUser);
    }
}
