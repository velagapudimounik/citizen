package com.drughub.citizen.orangewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.notifications.NotificationActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.utils.DrughubIcon;

public class SaveCard extends BaseActivity {

    DrughubIcon btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orange_wallet_saved_cards_list);
        setTitle("Saved Cards");
        setBackButton(true);


        btn = (DrughubIcon) findViewById(R.id.icon_right);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        SavedcardsAdpter savedcardsAdpter = new SavedcardsAdpter(this);
        recyclerView.setAdapter(savedcardsAdpter);


        addActionButton(R.string.icon_notification);

    }

    public void onActionButtonClicked(int drughubIconsRes) {
        super.onActionButtonClicked(drughubIconsRes);


        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(intent);
    }


    public void onAddCardClick(View v) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.addCardLayout);
        if (ll.getVisibility() == View.VISIBLE) {
            btn.setRotation(270);
            setTitle("Saved Cards");
            ll.setVisibility(View.GONE);
        } else {
            btn.setRotation(360);
            setTitle("Add card");
            ll.setVisibility(View.VISIBLE);
        }
    }
}






