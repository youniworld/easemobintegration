package com.example.youni.testapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.youni.testapp.MainActivity;
import com.example.youni.testapp.R;
import com.hyphenate.chat.EMClient;

/**
 * Created by youni on 2016/5/18.
 */
public class SplashActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler handler =  new Handler() {
            public void handleMessage(Message msg) {
                onInit();
            }
        };

        handler.sendMessageDelayed(Message.obtain(),3*1000);
    }

    private void onInit() {

        if(EMClient.getInstance().isLoggedInBefore()){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
