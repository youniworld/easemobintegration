package com.atguigu.imdemo.model;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.atguigu.imdemo.model.db.DBManager;
import com.atguigu.imdemo.model.db.PreferenceUtils;
import com.atguigu.imdemo.model.db.UserAccountDB;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youni on 2016/6/15.
 * 微信:youniworld
 * 作用：
 */
public class IMModel {
    private static IMModel instance = null;
    private Context appContext;
    private PreferenceUtils preferenceUtils;
    private UserAccountDB userAccountDB;
    private DBManager dbManager;
    private List<EMContactListener> contactListenerList = new ArrayList<>();
    private Map<String,IMUser> contacts = new HashMap<>();
    private Handler mH = new Handler();

    // 获得单例模型
    static public IMModel getInstance(){
        if(instance == null){
            instance = new IMModel();
        }
        return instance;
    }

    // 初始化EaseUI和环信SDK
    public void init(Context context){
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);

        appContext = context;

        if(!EaseUI.getInstance().init(appContext, options)){
            return ;
        }

        preferenceUtils = new PreferenceUtils(context);
        userAccountDB = new UserAccountDB(context);

        initListener();
    }

    public void addAccount(IMUser account){
        userAccountDB.addAccount(account);
    }

    public IMUser getAccount(String appUser){
       return userAccountDB.getAccount(appUser);
    }

    public IMUser getAccountByHXID(String hxId){
        return  userAccountDB.getAccountByHXID(hxId);
    }

    public void addContactListener(EMContactListener contactListener){
        if(!contactListenerList.contains(contactListener)){
            contactListenerList.add(contactListener);
        }
    }

    public void onLoggedIn(String hxId){
        dbManager = new DBManager(appContext,hxId);
    }

    public IMUser getUserFromAppServer(String hxId){
        IMUser user = new IMUser(hxId);

        return user;
    }

    public List<InvitationInfo> getInvitationList(){
        return dbManager.getInvitationList();
    }

    private String getAppUserById(String hxId){
        for(String appUser:contacts.keySet()){
            IMUser user = contacts.get(appUser);

            if(user.getHxId() == hxId){
                return appUser;
            }
        }

        return null;
    }

    private void initListener(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                IMUser user = getUserFromAppServer(s);

                contacts.put(user.getAppUser(),user);

                for(EMContactListener listener:contactListenerList){
                    listener.onContactRefused(s);
                }
            }

            @Override
            public void onContactDeleted(String s) {

                String appUser = getAppUserById(s);

                if(s != null){
                    contacts.remove(s);
                }

                for (EMContactListener listener:contactListenerList){
                    listener.onContactDeleted(s);
                }
            }

            @Override
            public void onContactInvited(final String s, String s1) {
                mH.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(appContext,"收到邀请 : " + s,Toast.LENGTH_LONG).show();
                    }
                });

                IMUser user = getUserFromAppServer(s);
                InvitationInfo inviteInfo = new InvitationInfo(user);

                inviteInfo.setInviateState(InvitationInfo.InviateState.INVITE_NEW);
                inviteInfo.setReason(s1);

                dbManager.updateInvitationInfo(inviteInfo);

                for(EMContactListener listener:contactListenerList){
                    listener.onContactInvited(s,s1);
                }
            }

            @Override
            public void onContactAgreed(final String s) {
                mH.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(appContext,s + " 同意了邀请 : ",Toast.LENGTH_LONG).show();
                    }
                });

                IMUser user = getUserFromAppServer(s);
                InvitationInfo info = new InvitationInfo(user);
                info.setInviateState(InvitationInfo.InviateState.INVITE_ACCEPT_BY_PEER);

                dbManager.updateInvitationInfo(info);

                for (EMContactListener listener:contactListenerList){
                    listener.onContactAgreed(s);
                }
            }

            @Override
            public void onContactRefused(final String s) {
                mH.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(appContext,s + " 拒绝了邀请 : ",Toast.LENGTH_LONG).show();
                    }
                });

                for(EMContactListener listener:contactListenerList){
                    listener.onContactRefused(s);
                }
            }
        });
    }
}
