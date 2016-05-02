package com.drughub.citizen.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;


public class NotificationActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        setTitle("Notifications");
        setBackButton(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler2);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        NotificationAdapter notificationAdapter = new NotificationAdapter(this);
        recyclerView.setAdapter(notificationAdapter);

        addActionButton(R.string.icon_settings);
    }

    public void onActionButtonClicked(int drughubIconRes) {
        super.onActionButtonClicked(drughubIconRes);

        Intent intent = new Intent(NotificationActivity.this, NotificationsettingActivity.class);
        startActivity(intent);
    }


    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Adptrinfo> {

        Context context;

        NotificationAdapter(Context l_context) {
            this.context = l_context;
        }

        @Override
        public Adptrinfo onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
            return new Adptrinfo(v);
        }

        @Override
        public void onBindViewHolder(Adptrinfo holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class Adptrinfo extends RecyclerView.ViewHolder {

            public Adptrinfo(View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(context, VaccActivity.class);
                        //context.startActivity(intent);
                    }
                });
            }
        }
    }
}
