package com.atguigu.imdemo.model;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class InvitationInfo {
    private IMUser appUser;
    private InviateState inviateState;
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InviateState getInviateState() {
        return inviateState;
    }

    public void setInviateState(InviateState inviateState) {
        this.inviateState = inviateState;
    }

    public IMUser getAppUser() {
        return appUser;
    }

    public void setAppUser(IMUser appUser) {
        this.appUser = appUser;
    }

    public InvitationInfo(IMUser appUser){
        this.appUser  = appUser;
        inviateState = InviateState.INVITE_NEW;
    }


    public enum InviateState{
        INVITE_NEW,
        INVITE_ACCEPT,
        INVITE_ACCEPT_BY_PEER
    }
}
