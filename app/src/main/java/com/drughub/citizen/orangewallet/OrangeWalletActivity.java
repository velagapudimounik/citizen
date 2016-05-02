package com.drughub.citizen.orangewallet;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.notifications.NotificationActivity;
import com.drughub.citizen.R;

public class OrangeWalletActivity extends BaseActivity {

    Fragment fragment = null;

    @Override

    public void onActionButtonClicked(int drughubIconsRes) {
        super.onActionButtonClicked(drughubIconsRes);


        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(intent);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orange_wallet_main);
        setTitle("Orange Wallet");
        setBackButton(true);

        addActionButton(R.string.icon_notification);


        final RadioButton btn1 = (RadioButton) findViewById(R.id.points);
        final RadioButton btn2 = (RadioButton) findViewById(R.id.Addmoney);
        final RadioButton btn3 = (RadioButton) findViewById(R.id.coupon);
        final TextView wallet_bal = (TextView) findViewById(R.id.wallet_bal);
        final TextView sav_Card = (TextView) findViewById(R.id.save_card);


        fragment = new OrangepointFragment();
        btn1.setBackgroundColor(Color.WHITE);
        btn2.setBackgroundColor(Color.LTGRAY);
        btn3.setBackgroundColor(Color.LTGRAY);
        sav_Card.setText("Saved Cards");
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction fragmenttransaction = fragmentmanager.beginTransaction();
        fragmenttransaction.replace(R.id.orange_container, fragment).commit();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new OrangepointFragment();
                btn1.setBackgroundColor(Color.WHITE);
                btn2.setBackgroundColor(Color.LTGRAY);
                btn3.setBackgroundColor(Color.LTGRAY);
                sav_Card.setText("Saved Cards");
                FragmentManager fragmentmanager = getSupportFragmentManager();
                FragmentTransaction fragmenttransaction = fragmentmanager.beginTransaction();
                fragmenttransaction.replace(R.id.orange_container, fragment).commit();
                // Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AddwalletFragment();
                btn2.setBackgroundColor(Color.WHITE);
                btn1.setBackgroundColor(Color.LTGRAY);
                btn3.setBackgroundColor(Color.LTGRAY);

                //wallet_bal.setText("Rs.0");
                sav_Card.setText("Saved Cards");
                FragmentManager fragmentmanager = getSupportFragmentManager();
                FragmentTransaction fragmenttransaction = fragmentmanager.beginTransaction();
                fragmenttransaction.replace(R.id.orange_container, fragment).commit();
                // Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CouponFragment();
                btn3.setBackgroundColor(Color.WHITE);
                btn2.setBackgroundColor(Color.LTGRAY);
                btn1.setBackgroundColor(Color.LTGRAY);
                sav_Card.setText("Saved Cards");
                //wallet_bal.setText("Rs.0");
                FragmentManager fragmentmanager = getSupportFragmentManager();
                FragmentTransaction fragmenttransaction = fragmentmanager.beginTransaction();
                fragmenttransaction.replace(R.id.orange_container, fragment).commit();
                // Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void onSavedCardClick(View v) {
        startActivity(new Intent(getApplicationContext(), SaveCard.class));

    }
}




