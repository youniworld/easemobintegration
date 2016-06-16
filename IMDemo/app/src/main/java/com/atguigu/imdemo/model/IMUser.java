package com.atguigu.imdemo.model;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class IMUser {
    private String appUser;
    private String hxId;
    private String avartar;
    private String nick;

    public IMUser(){

    }
    public IMUser(String appUser){
        this.appUser = appUser;
        this.nick = appUser;
        this.hxId = appUser;
    }

    public String getHxId() {
        return hxId;
    }

    public void setHxId(String hxId) {
        this.hxId = hxId;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }

    public String getNick() {
        if(nick == null){
            return hxId;
        }
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }
}
