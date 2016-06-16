package com.atguigu.imdemo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.imdemo.R;
import com.atguigu.imdemo.model.IMModel;
import com.atguigu.imdemo.model.IMUser;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by youni on 2016/6/15.
 * 微信:youniworld
 * 作用：
 */
public class LoginActivity extends Activity {
    private EditText userNameEditText;
    private EditText pwdEditText;
    private Activity me;
    private IMUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        me = this;
        setContentView(R.layout.activtiy_login);

        initView();
    }

    void initView(){
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button registerBtn = (Button) findViewById(R.id.btn_register);

        loginBtn.setOnClickListener(btnClickListener);
        registerBtn.setOnClickListener(btnClickListener);

        userNameEditText = (EditText) findViewById(R.id.et_username);
        pwdEditText = (EditText) findViewById(R.id.et_pwd);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = userNameEditText.getText().toString();
            String pwd = pwdEditText.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            if(v.getId() == R.id.btn_register){
                register(name,pwd);
            }else{
                login(name,pwd);
            }
        }
    };

    private void register(final String userName, final String pwd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currentUser = IMModel.getInstance().getAccount(userName);

                    if(currentUser == null) {
                        currentUser = createAppAccount(userName, pwd);
                        IMModel.getInstance().addAccount(currentUser);
                    }

                    EMClient.getInstance().createAccount(currentUser.getHxId(), pwd);

                    me.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(me, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    final String error = e.toString();
                    me.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(me,"注册失败 : " + error,Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }).start();
    }

    private IMUser createAppAccount(String username, String pwd){
        IMUser user = new IMUser(username);
        user.setAppUser(username);
        return user;
    }

    private IMUser getAppAccount(String appUser){
        return new IMUser(appUser);
    }

    private void login(final String appUser, final String  pwd){
        final ProgressDialog pd = new ProgressDialog(me);

        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                IMUser user = IMModel.getInstance().getAccount(appUser);

                if(user == null){
                    user = getAppAccount(appUser);
                    if(user == null){
                        me.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(me,"没有次账号",Toast.LENGTH_LONG).show();
                                pd.cancel();
                            }
                        });

                        return;
                    }

                    loginHX(user,pwd,pd);
                }else{
                    loginHX(user,pwd,pd);
                }
            }
        }).start();
    }

    void loginHX(final IMUser user, final String pwd, final ProgressDialog pd){
        EMClient.getInstance().login(user.getHxId(), pwd, new EMCallBack() {
            @Override
            public void onSuccess() {

                me.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IMModel.getInstance().onLoggedIn(user.getHxId());
                        IMModel.getInstance().addAccount(user);

                        pd.cancel();
                        //启动主界面
                        me.startActivity(new Intent(me, MainActivity.class));

                        finish();
                    }
                });
            }

            @Override
            public void onError(int i, final  String s) {
                me.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(me,"登陆失败 : " + s,Toast.LENGTH_LONG).show();
                        pd.cancel();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
