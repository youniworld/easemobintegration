package com.atguigu.imdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.atguigu.imdemo.R;
import com.hyphenate.chat.EMClient;

/**
 * Created by youni on 2016/6/15.
 * 微信:youniworld
 * 作用：
 */
public class SplashActivity extends Activity {
    private Handler mH = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        init();
    }

    void init(){
        mH.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(EMClient.getInstance().isLoggedInBefore()){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
                finish();
            }
        },3*1000);
    }
}
