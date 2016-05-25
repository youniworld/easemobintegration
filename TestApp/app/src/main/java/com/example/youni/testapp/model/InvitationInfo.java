package com.example.youni.testapp.model;

/**
 * Created by youni on 2016/5/25.
 */
public class InvitationInfo {
    public DemoUser getUser() {
        return user;
    }

    public void setUser(DemoUser user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private DemoUser user;
    private String reason;

    public InvitationInfo(String reason, DemoUser user){
        this.user = user;
        this.reason = reason;
    }

    public InvitationInfo(){

    }
}
