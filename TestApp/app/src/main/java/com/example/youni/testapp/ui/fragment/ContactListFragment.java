package com.example.youni.testapp.ui.fragment;

import android.view.View;

import com.example.youni.testapp.R;
import com.example.youni.testapp.model.Model;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youni on 2016/5/19.
 */
public class ContactListFragment extends EaseContactListFragment {

    private Model.OnSyncListener mContactSyncListener;

    @Override
    public void setUpView(){
        super.setUpView();
        titleBar.setRightImageResource(R.drawable.em_add);

        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mContactSyncListener = new Model.OnSyncListener() {
            @Override
            public void onSuccess() {
                setupContacts();
            }

            @Override
            public void onFailed() {

            }
        };

        Model.getInstance().addOnContactSyncListener(mContactSyncListener);

        if(Model.getInstance().isContactSynced()){
            setupContacts();
        }
    }

    private void setupContacts(){
        Map<String,EaseUser> easeUsers = new HashMap<>();

        Map<String,Model.DemoUser> appUsers = Model.getInstance().getContacts();

        if(appUsers != null){
            for(Model.DemoUser user:appUsers.values()){
                EaseUser easeUser = new EaseUser(user.hxId);

                easeUsers.put(user.hxId,easeUser );
            }
            setContactsMap(easeUsers);
            refresh();
        }
    }

    @Override
    public void initView(){
        super.initView();
    }
}
