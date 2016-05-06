package com.drughub.citizen.bookappointments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.drughub.citizen.R;
import com.drughub.citizen.orangewallet.OrangepointFragment;


public class BookActivity extends Fragment {

    TabLayout tablayout;
    Book book;
    RadioGroup radioGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_appointment, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         tablayout = (TabLayout) view.findViewById(R.id.tab_layout);
        getActivity().setTitle(getString(R.string.book_appointments));

       // getFragmentManager().beginTransaction().replace(R.id.pager,book=new Book()).commit();
      getFragmentManager().beginTransaction().replace(R.id.pager,new Book()).commit();


        radioGroup=(RadioGroup) view.findViewById(R.id.Radiogrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.book:
                        getFragmentManager().beginTransaction().replace(R.id.pager,new Book()).commit();
                        break;
                    case R.id.booked:
                        getFragmentManager().beginTransaction().replace(R.id.pager,new Booked()).commit();
                        break;
                    case R.id.cancelled:
                        getFragmentManager().beginTransaction().replace(R.id.pager,new Cancelled()).commit();
                        break;

                }
            }
        });


    }

    public static Book newInstance(boolean screen) {
        Book book1 = new Book();
        return book1;
    }

    public void initTabControl(View v) {



    }


  /*    public void initTabControl() {
            tablayout.addTab(tablayout.newTab().setText("Book"));
            tablayout.addTab(tablayout.newTab().setText("Booked"));
            tablayout.addTab(tablayout.newTab().setText("Cancelled"));


            tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {


                    // inorder to avoid getting the fragment back. If entered inside fragment1 and then went to fragment2, if u press back in frag2 it shouldnot go to tab1.

                    for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++)

                        getFragmentManager().popBackStackImmediate();



//                popBackStack(null//getSupportFragmentManager().getBackStackEntryAt(i).getId()
//                        , FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Fragment fragment = new Book();
                    switch (tab.getPosition()) {
                        case 0:

                            fragment   = new Book();
                            break;
                        case 1:

                            fragment= new Booked();
                            break;

                        case 2:

                            fragment = new Cancelled();
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
