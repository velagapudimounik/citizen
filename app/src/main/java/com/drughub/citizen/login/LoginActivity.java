package com.drughub.citizen.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.MyApplication;
import com.drughub.citizen.R;

public class LoginActivity extends BaseActivity {
    Fragment fragment=null;
//    private Realm realm;
//    String json = "{" +
//            "\"name\" : \"Test\"," +
//            "\"email\" : \"test@test.com\"," +
//            "\"mobile\" : \"9876543210\"," +
//            "\"password\" : \"test@123\"" +
//            "}";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);
        MyApplication.get().removeSessionCookie();
//        realm = getRealmObject();
//        realm.beginTransaction();
//        User user = realm.createObjectFromJson(User.class, json);
//        RealmResults<User> user1=realm.allObjects(User.class);
//        String name  = user.getName();
//        String email  = user.getEmail();
//        String mobile  = user.getMobile();
//        String pass  = user.getPassword();

//        realm.commitTransaction();

        fragment=new LoginPage();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.container1, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        realm.close();
    }
}
