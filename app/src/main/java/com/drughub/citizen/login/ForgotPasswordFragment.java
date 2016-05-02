package com.drughub.citizen.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.drughub.citizen.R;
import com.drughub.citizen.model.User;
import com.drughub.citizen.network.Globals;


public class ForgotPasswordFragment extends Fragment {

    Fragment fragment = null;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.forgotPassword));
        final View view = inflater.inflate(R.layout.login_forgotpassword, container, false);

        TextView text = (TextView) view.findViewById(R.id.reset);
        final EditText editEmail = (EditText) view.findViewById(R.id.emailForForgot);
        Button btn = (Button) view.findViewById(R.id.submitButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                if (Globals.isValidEmail(email)) {
                    User user = new User();
                    user.setEmailId(email);
                    user.ForgetPassword(getActivity(), new Globals.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                    fragment = new ThanksRegards();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String result) {
                        }
                    });
                }


            }
        });
        return view;
    }
}
