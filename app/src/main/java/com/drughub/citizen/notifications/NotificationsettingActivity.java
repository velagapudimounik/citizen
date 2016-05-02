package com.drughub.citizen.notifications;

import android.os.Bundle;
import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;

public class NotificationsettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationsetting);
        setTitle("Notification Settings");
        setBackButton(true);
    }
}













