package com.drughub.citizen.vaccinationFAQs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.drughub.citizen.BaseActivity;
import com.drughub.citizen.R;

/**
 * Created by Achyuth V on 5/5/2016.
 */
public class VaccinationContainer extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccination_container);
        setTitle("Vaccination FAQs");
        setBackButton(true);
        Fragment fragment = new VaccinationListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.vaccinationContianer,fragment).commit();
    }
}
