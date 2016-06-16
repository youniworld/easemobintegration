package com.atguigu.imdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.imdemo.R;
import com.atguigu.imdemo.model.IMUser;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class AddContactActivity extends Activity {
    Button searchButton;
    Button addButton;
    TextView contactTextView;
    LinearLayout linearLayoutAddContact;
    TextView searchedContact;
    Activity me;
    IMUser foundUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);
        me = this;

        findView();
        initView();

    }

    void findView(){
        linearLayoutAddContact = (LinearLayout) findViewById(R.id.ll_add_contact);
        linearLayoutAddContact.setVisibility(View.INVISIBLE);

        contactTextView = (TextView) findViewById(R.id.et_contact);

        searchButton = (Button) findViewById(R.id.btn_search_contact);

        searchedContact = (TextView) findViewById(R.id.tv_searched_contact);

        addButton = (Button) findViewById(R.id.btn_add_contact);
    }

    void initView(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = contactTextView.getText().toString();

                if(TextUtils.isEmpty(appName)){
                    Toast.makeText(me,"搜索的联系人不能为空",Toast.LENGTH_LONG).show();

                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        foundUser = searchNameFromAppServer(appName);

                        me.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linearLayoutAddContact.setVisibility(View.VISIBLE);
                                searchedContact.setText(foundUser.getNick());
                            }
                        });
                    }
                }).start();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hxId = foundUser.getHxId();

                if(TextUtils.isEmpty(hxId)){
                    Toast.makeText(AddContactActivity.this,"添加的联系人不能为空!",Toast.LENGTH_LONG).show();
                    return;
                }

                addContact(hxId);
            }
        });
    }

    private IMUser searchNameFromAppServer(String appName) {
        return new IMUser(appName);
    }

    void addContact(final String hxId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(hxId,"加个好友吧");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
