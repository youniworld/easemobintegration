package com.example.youni.testapp;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by youni on 2016/5/18.
 */
public class testApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        EMOptions options = new EMOptions();

        //options.setAutoLogin(true);
        // 如果我们不基于EaseUI做开发，我们可以直接用EMClient.getInstance().init初始化App
        EaseUI.getInstance().init(this,options);
    }
}
