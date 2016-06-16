package com.atguigu.imdemo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.atguigu.imdemo.R;
import com.atguigu.imdemo.ui.fragment.ContactListFragment;
import com.atguigu.imdemo.ui.fragment.SettingsFragment;

public class MainActivity extends FragmentActivity {
    private SettingsFragment settingsFragment;
    private ContactListFragment contactListFragment;
    private RadioGroup radioGroup;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        radioGroup = (RadioGroup) findViewById(R.id.rg_main_tab);

        radioGroup.check(R.id.rb_settings);
        settingsFragment = new SettingsFragment();
        contactListFragment = new ContactListFragment();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch(checkedId){
                    case R.id.rb_conversation_list:
                        break;
                    case R.id.rb_contact_list:
                        fragment = contactListFragment;
                        break;
                    case R.id.rb_settings:
                        fragment = settingsFragment;
                        break;
                }

                if(fragment == null){
                    return;
                }

                if(currentFragment != fragment){
                    switchToFragment(fragment);
                }
            }
        });

        switchToFragment(settingsFragment);
    }

    private void switchToFragment(Fragment fragment){
        currentFragment = fragment;

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_fragment_container,fragment);
        ft.show(fragment);
        ft.commit();
    }
}
