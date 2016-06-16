package com.atguigu.imdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.imdemo.R;
import com.atguigu.imdemo.model.IMModel;
import com.atguigu.imdemo.model.IMUser;
import com.atguigu.imdemo.model.InvitationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class InvitationListActivity extends Activity implements OnInvirationButtonClickListener{
    private InvitationAdapter invitationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invitation_list);

        init();
    }

    private void init(){
        invitationAdapter = new InvitationAdapter(this);
        ListView listView = (ListView) findViewById(R.id.lv_invitation_list);

        listView.setAdapter(invitationAdapter);

        setupInvitationInfos();
    }

    private void setupInvitationInfos(){
        invitationAdapter.setupInvitationInfos(IMModel.getInstance().getInvitationList());
    }

    @Override
    public void onAccepted(IMUser user) {

    }

    @Override
    public void onRejected(IMUser user) {

    }
}

class InvitationAdapter extends BaseAdapter{
    private List<InvitationInfo> invitationInfos;
    private Context context;
    public InvitationAdapter(Context context){
        this.context = context;
    }

    public void setupInvitationInfos(List<InvitationInfo> infos){
        invitationInfos = new ArrayList<>();

        invitationInfos.addAll(infos);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(invitationInfos == null){
            return 0;
        }
        return invitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        if(invitationInfos == null){
            return null;
        }
        return invitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            convertView = View.inflate(context,R.layout.row_invitation_item,null);

            viewHolder = new ViewHolder();
            viewHolder.tvContact = (TextView) convertView.findViewById(R.id.tv_invitation_friend);
            viewHolder.tvReason = (TextView) convertView.findViewById(R.id.tv_invitation_reason);
            viewHolder.btnAccept = (Button) convertView.findViewById(R.id.btn_invitation_accept);
            viewHolder.btnReject = (Button) convertView.findViewById(R.id.btn_invitation_reject);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InvitationInfo info = invitationInfos.get(position);

        viewHolder.tvContact.setText(info.getAppUser().getNick());
        if(info.getInviateState() == InvitationInfo.InviateState.INVITE_NEW){
            viewHolder.btnReject.setVisibility(View.VISIBLE);
            viewHolder.btnReject.setVisibility(View.VISIBLE);

            viewHolder.tvReason.setText(info.getReason());
        }else{
            viewHolder.btnReject.setVisibility(View.GONE);
            viewHolder.btnReject.setVisibility(View.GONE);
            if(info.getInviateState() == InvitationInfo.InviateState.INVITE_ACCEPT){
                viewHolder.tvReason.setText("你接受了邀请");
            }else{
                viewHolder.tvReason.setText("接受了你的邀请");
            }
        }
        return convertView;
    }

    static class ViewHolder{
        TextView tvContact;
        TextView tvReason;
        Button btnAccept;
        Button btnReject;
    }
}

interface OnInvirationButtonClickListener{
    void onAccepted(IMUser user);
    void onRejected(IMUser user);
}