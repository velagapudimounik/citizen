package com.drughub.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drughub.citizen.login.LoginActivity;
import com.drughub.citizen.model.User;
import com.drughub.citizen.myprofile.MyProfileActivity;
import com.drughub.citizen.orangeconnect.OrangeConnectActivity;
import com.drughub.citizen.patientrecords.PatientRecordActivity;
import com.drughub.citizen.vaccinationFAQs.VaccinationContainer;

import io.realm.Realm;

public class MoreFragment extends Fragment {

    Fragment fragment = null;
    User userProfile;
    User user;
    private TextView profileDoctorname, profileDoctorcode;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more, container, false);
        profileDoctorname = (TextView) view.findViewById(R.id.profileDoctorName);
        profileDoctorcode = (TextView) view.findViewById(R.id.profileDoctorCode);
        LinearLayout patient_records = (LinearLayout) view.findViewById(R.id.patient_records);
        LinearLayout orangeConnect = (LinearLayout) view.findViewById(R.id.orange_connect);
        LinearLayout about = (LinearLayout) view.findViewById(R.id.about);

        TextView shareApp = (TextView) view.findViewById(R.id.shareapp);
        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Doctor App: https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.send_to)));
            }
        });

        patient_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PatientRecordActivity.class));
            }
        });

        orangeConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OrangeConnectActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });

        View vaccinationfaq = view.findViewById(R.id.vaccination_faq);
        vaccinationfaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VaccinationContainer.class));

            }
        });

        View myprofile = view.findViewById(R.id.my_profile);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
            }
        });

        View logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        userProfile = realm.where(User.class).findFirst();
        if (userProfile != null) {
            profileDoctorname.setText(userProfile.getFirstName());
            profileDoctorcode.setText("" + userProfile.getUserProfileId());
        }
    }
}
