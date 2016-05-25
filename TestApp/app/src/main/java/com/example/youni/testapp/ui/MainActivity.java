package com.example.youni.testapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.youni.testapp.R;
import com.example.youni.testapp.model.DemoUser;
import com.example.youni.testapp.model.Model;
import com.example.youni.testapp.ui.fragment.ContactListFragment;
import com.example.youni.testapp.ui.fragment.ConversationListFragment;
import com.example.youni.testapp.ui.fragment.SettingsFragment;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class MainActivity extends FragmentActivity {

    Fragment mSetttingsFragment;
    Fragment mConversationListFragment;
    ContactListFragment mContactListFragment;
    Model.OnSyncListener mContactSyncListener;
    int currentId;
    EMContactListener mContactListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup group = (RadioGroup) findViewById(R.id.tab_group);

        mSetttingsFragment = new SettingsFragment();
        mConversationListFragment = new ConversationListFragment();
        mContactListFragment = new ContactListFragment();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (currentId != checkedId) {
                    currentId = checkedId;

                    Fragment fragment = null;

                    switch (currentId) {
                        case R.id.conv_list_btn:
                            fragment = mConversationListFragment;
                            break;

                        case R.id.contact_list_btn:
                            fragment = mContactListFragment;
                            break;

                        case R.id.setting_btn:
                            fragment = mSetttingsFragment;
                            break;

                        default:
                            fragment = mSetttingsFragment;
                    }

                    switchFragment(fragment);
                }
            }
        });

        group.check(R.id.conv_list_btn);

        currentId = R.id.conv_list_btn;

        switchFragment(mConversationListFragment);

        if(!Model.getInstance().isContactSynced()){
            Model.getInstance().asyncfetchUsers();
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fragment_main, fragment);
        ft.show(fragment);

        ft.commit();
    }

    void initListener(){
        mContactListener = new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                DemoUser user = new DemoUser();

                user.hxId = s;

                Model.getInstance().addUser(user);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContactListFragment.setupContacts();
                    }
                });
            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(s);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onContactAgreed(final String s) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,s + "同意了您的好友请求",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onContactRefused(String s) {

            }
        };

        EMClient.getInstance().contactManager().setContactListener(mContactListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        EMClient.getInstance().contactManager().removeContactListener(mContactListener);
    }
}