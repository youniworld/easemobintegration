package com.example.youni.testapp.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.youni.testapp.R;
import com.example.youni.testapp.model.DemoUser;
import com.example.youni.testapp.model.Model;
import com.example.youni.testapp.ui.AddFriendActivity;
import com.example.youni.testapp.ui.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
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
                getActivity().startActivity(new Intent(getActivity(), AddFriendActivity.class));
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
            Log.d("ContactListFragment", "already synced");
            Model.getInstance().loadLocalContacts();
            setupContacts();
        }
    }

    public void setupContacts(){
        Map<String,EaseUser> easeUsers = new HashMap<>();

        Map<String,DemoUser> appUsers = Model.getInstance().getContacts();

        if(appUsers != null){
            for(DemoUser user:appUsers.values()){
                EaseUser easeUser = new EaseUser(user.hxId);
                easeUser.setNick(user.userName);

                easeUsers.put(user.hxId,easeUser );
            }
            setContactsMap(easeUsers);
            refresh();
        }
    }

    @Override
    public void initView(){
        super.initView();

        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                Log.d("ContactListFragment", "onListItemClicked");

                getActivity().startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });
    }
}
