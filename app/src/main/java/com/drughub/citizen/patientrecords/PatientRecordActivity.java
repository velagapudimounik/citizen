package com.drughub.citizen.patientrecords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.DrughubIcon;

public class PatientRecordActivity extends BaseActivity {

    public DrughubIcon bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_record_activity);
        setBackButton(true);
        setTitle(getResources().getString(R.string.patientrecords));
        bookmark = (DrughubIcon)addActionButton(R.string.icon_bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookmarkedActivity.class));
            }
        });
        PatientRecordFragment fragment = new PatientRecordFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, fragment).commit();
//        changeFragment(fragment);

    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, fragment).addToBackStack(fragment.getClass().getName()).commit();
    }

    public void hideButton(boolean value)
    {
        if(value)
           bookmark.setVisibility(View.GONE);
        else
          bookmark.setVisibility(View.VISIBLE);

    }
}
