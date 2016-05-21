package com.example.youni.testapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.youni.testapp.R;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * Created by youni on 2016/5/20.
 */
public class ChatActivity extends FragmentActivity{
    private EaseChatFragment mChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatFragment = new EaseChatFragment();

        mChatFragment.setArguments(getIntent().getExtras());

        setContentView(R.layout.activity_chat);

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fragment_chat,mChatFragment);

        ft.commit();

    }
}
