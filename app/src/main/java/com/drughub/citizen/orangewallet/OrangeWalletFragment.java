package com.drughub.citizen.orangewallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.drughub.citizen.R;
import com.drughub.citizen.bookappointments.Book;
import com.drughub.citizen.bookappointments.Booked;
import com.drughub.citizen.bookappointments.Cancelled;

public class OrangeWalletFragment extends Fragment {


    OrangepointFragment point;
    RadioGroup toggleRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orange_wallet_main, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.orange_wallet));

        getFragmentManager().beginTransaction().replace(R.id.orange_container, new OrangepointFragment()).commit();


        toggleRadio=(RadioGroup) view.findViewById(R.id.toggle2);

        toggleRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {

                    case R.id.points:
                        getFragmentManager().beginTransaction().replace(R.id.orange_container, new OrangepointFragment()).commit();
                        break;
                    case R.id.Addmoney:
                        getFragmentManager().beginTransaction().replace(R.id.orange_container, new AddwalletFragment()).commit();
                        break;
                    case R.id.coupon:
                        getFragmentManager().beginTransaction().replace(R.id.orange_container, new CouponFragment()).commit();
                        break;
                }
            }
        });

/*
        points=(RadioButton)view.findViewById(R.id.points);
        Addmoney=(RadioButton)view.findViewById(R.id.Addmoney);
        coupon=(RadioButton)view.findViewById(R.id.coupon);


        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.orange_container,new OrangepointFragment()).commit();
            }
        });

        Addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.orange_container,new AddwalletFragment()).commit();


            }
        });

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.orange_container,new CouponFragment()).commit();

            }
        });*/


        //  initTabControl();
        //getFragmentManager().beginTransaction().replace(R.id.pager,point= new OrangepointFragment()).commit();


    }

/*
    public void initTabControl() {
        tabs.addTab(tabs.newTab().setText("Orange Points"));
        tabs.addTab(tabs.newTab().setText("Add Money"));
        tabs.addTab(tabs.newTab().setText("Coupons"));


        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                // inorder to avoid getting the fragment back. If entered inside fragment1 and then went to fragment2, if u press back in frag2 it shouldnot go to tab1.

                for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++)

                    getFragmentManager().popBackStackImmediate();


//                popBackStack(null//getSupportFragmentManager().getBackStackEntryAt(i).getId()
//                        , FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment fragment = new OrangepointFragment();
                switch (tab.getPosition()) {
                    case 0:

                        fragment = new OrangepointFragment();
                        break;
                    case 1:

                        fragment = new AddwalletFragment();
                        break;

                    case 2:

                        fragment = new CouponFragment();
                        break;

                }
                getFragmentManager().beginTransaction().replace(R.id.pager, fragment).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }*/
}