package com.atguigu.imdemo;

import android.app.Application;

import com.atguigu.imdemo.model.IMModel;

/**
 * Created by youni on 2016/6/15.
 * 微信:youniworld
 * 作用：
 */
public class IMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        IMModel.getInstance().init(this);
    }
}
