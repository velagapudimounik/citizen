package com.drughub.citizen.login;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.drughub.citizen.R;

public class ThanksRegards extends Fragment {

    Fragment fragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_password_reset_success, container, false);
        getActivity().setTitle(getResources().getString(R.string.forgotPassword));

        Button btnok = (Button) view.findViewById(R.id.okButton);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container1, new LoginPage()).commit();
                getActivity().getSupportFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;

    }
}
