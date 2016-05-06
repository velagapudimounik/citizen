package com.drughub.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drughub.citizen.bookappointments.BookActivity;
import com.drughub.citizen.notifications.NotificationActivity;
import com.drughub.citizen.myfamily.MyFamilyFragment;
import com.drughub.citizen.orangewallet.OrangeWalletFragment;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabControl();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyFamilyFragment()).commit();
        addActionButton(R.string.icon_notification);
    }

    @Override
    public void onActionButtonClicked(int drughubIconRes) {
        super.onActionButtonClicked(drughubIconRes);

        switch (drughubIconRes) {
            case R.string.icon_notification:
                //show notifications
                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
                break;
        }
    }

    private TabLayout.Tab addTab(TabLayout tabLayout, int textRes, int iconRes) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setCustomView(R.layout.main_tab_view);

        View view = tab.getCustomView();
        TextView tabName = (TextView) view.findViewById(R.id.tabName);
        tabName.setText(getString(textRes));

        ImageView tabIcon = (ImageView) view.findViewById(R.id.tabIcon);
        tabIcon.setBackgroundResource(iconRes);

//        DrughubIcon tabIcon = (DrughubIcon)view.findViewById(R.id.tabIcon);
//        tabIcon.setText(getResources().getString(iconRes));

        tabLayout.addTab(tab);

        return tab;
    }

    private void initTabControl() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

//        addTab(tabLayout, R.string.my_inventory, R.string.icon_my_inventory);
//        addTab(tabLayout, R.string.consultations, R.string.icon_consultations);
//        addTab(tabLayout, R.string.analytics, R.string.icon_analytics);
//        addTab(tabLayout, R.string.more, R.string.icon_more);

        addTab(tabLayout, R.string.my_family, R.drawable.ic_action_1);
        addTab(tabLayout, R.string.book_appointments, R.drawable.ic_action_2);
        addTab(tabLayout, R.string.orange_wallet, R.drawable.ic_action_3);
        addTab(tabLayout, R.string.more, R.drawable.ic_action_4);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                clearActionButtons();
                setActionBarVisibility(true);

                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new MyFamilyFragment();
                        addActionButton(R.string.icon_notification);
                        break;
                    case 1:
                        fragment = new BookActivity();
                        addActionButton(R.string.icon_notification);
                        break;
                    case 2:
                        fragment = new OrangeWalletFragment();
                        addActionButton(R.string.icon_notification);
                        break;
                    case 3:
                        fragment = new MoreFragment();
                        setActionBarVisibility(false);
                        break;
                }
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
