package com.atguigu.imdemo.model;

import android.content.Context;

import com.atguigu.imdemo.model.db.PreferenceUtils;
import com.atguigu.imdemo.model.db.UserAccountDB;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

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
    }

    public void addAccount(IMUser account){
        userAccountDB.addAccount(account);
    }

    public IMUser getAccount(String appUser){
       return userAccountDB.getAccount(appUser);
    }
}
