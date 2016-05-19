package com.example.youni.testapp;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by youni on 2016/5/18.
 */
public class testApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        EMOptions options = new EMOptions();

        //options.setAutoLogin(true);

        EMClient.getInstance().init(this,options);
    }
}
