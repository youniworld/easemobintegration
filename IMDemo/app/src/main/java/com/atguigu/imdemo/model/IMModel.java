package com.atguigu.imdemo.model;

import android.content.Context;

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
    }
}
