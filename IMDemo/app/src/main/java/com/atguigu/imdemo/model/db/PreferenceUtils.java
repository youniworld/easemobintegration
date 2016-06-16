package com.atguigu.imdemo.model.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.atguigu.imdemo.model.IMUser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by youni on 2016/6/16.
 * 微信:youniworld
 * 作用：
 */
public class PreferenceUtils {
    private static final String NAME = "_atguigu";
    private SharedPreferences preferences;

    public PreferenceUtils(Context context){
        preferences = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    public void addUserAccount(IMUser me){
        HashSet<String> hashSet = new HashSet<>();

        hashSet.add(me.getHxId());
        hashSet.add(me.getNick());

        if(me.getAvartar() == null){
            hashSet.add("");
        }else{
            hashSet.add(me.getAvartar());
        }

        preferences.edit().putStringSet(me.getAppUser(),hashSet).commit();
    }

    public IMUser getAccount(String appUser){
        Set<String> value = preferences.getStringSet(appUser, null);

        if(value == null){
            return null;
        }

        IMUser user = new IMUser(appUser);

        Iterator<String> iterator = value.iterator();

        user.setHxId(iterator.next());
        user.setNick(iterator.next());
        user.setAvartar(iterator.next());
        return user;
    }
}
