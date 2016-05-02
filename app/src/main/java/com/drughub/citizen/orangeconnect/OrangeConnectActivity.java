package com.drughub.citizen.orangeconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;


public class OrangeConnectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_record_activity);
        setBackButton(true);
        setTitle(getResources().getString(R.string.orangeconnect));
        OrangeConnectFragment fragment = new OrangeConnectFragment();
//        changeFragment(fragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, fragment).commit();

    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, fragment).addToBackStack(fragment.getClass().getName()).commit();
    }

}
