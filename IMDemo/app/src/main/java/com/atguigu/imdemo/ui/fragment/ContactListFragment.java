package com.atguigu.imdemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.imdemo.R;
import com.atguigu.imdemo.ui.AddContactActivity;
import com.atguigu.imdemo.ui.InvitationListActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class ContactListFragment extends EaseContactListFragment {
    @Override
    protected void setUpView() {
        super.setUpView();

        titleBar.setRightImageResource(R.drawable.contact_add);

        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });

        View headerView = View.inflate(getActivity(),R.layout.fragment_contact_list_notification_header,null);

        listView.addHeaderView(headerView);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(),InvitationListActivity.class));
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
    }
}
