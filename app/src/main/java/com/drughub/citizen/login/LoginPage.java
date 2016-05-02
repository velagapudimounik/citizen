package com.drughub.citizen.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drughub.citizen.MainActivity;
import com.drughub.citizen.R;
import com.drughub.citizen.model.User;
import com.drughub.citizen.network.Globals;
import com.drughub.citizen.utils.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class LoginPage extends Fragment {

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final boolean ONLY_UI = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_layout, container, false);

        getActivity().setTitle(getResources().getString(R.string.login));

        final EditText editTextPassword = (EditText) view.findViewById(R.id.passwordTextField);
        final EditText editTextUserName = (EditText) view.findViewById(R.id.userNameTextField);
        editTextPassword.setTypeface(Typeface.DEFAULT);

        editTextUserName.setText(PrefUtils.getUserName(getActivity()));
        editTextPassword.setText(PrefUtils.getPassword(getActivity()));

        final TextView forgotPasswordTextView = (TextView) view.findViewById(R.id.forgotPasswordTextView);
        final Button loginButton = (Button) view.findViewById(R.id.loginButton);
        final Button signUpButton = (Button) view.findViewById(R.id.signUpButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == loginButton) {
                    if(ONLY_UI) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                        return;
                    }

                    String username = editTextUserName.getText().toString();
                    String password = editTextPassword.getText().toString();

                    if (username.isEmpty())
                        Toast.makeText(getActivity(),getString(R.string.EnterEmail) , Toast.LENGTH_SHORT).show();
                    else if (!Globals.isValidEmail(username))
                        Toast.makeText(getActivity(),getString(R.string.EnterValidEmail) , Toast.LENGTH_SHORT).show();
                    else if (password.isEmpty())
                        Toast.makeText(getActivity(), getString(R.string.EnterPassword), Toast.LENGTH_SHORT).show();
                     else
                        signIn(username, password);

                } else if (view == signUpButton) {
                    getFragmentManager().beginTransaction().replace(R.id.container1, new SignUpFragment()).addToBackStack(null).commit();
                } else if (view == forgotPasswordTextView) {
                    getFragmentManager().beginTransaction().replace(R.id.container1, new ForgotPasswordFragment()).addToBackStack(null).commit();
                }
            }
        };

        loginButton.setOnClickListener(listener);
        signUpButton.setOnClickListener(listener);
        forgotPasswordTextView.setOnClickListener(listener);

        return view;
    }

    private void signIn(final String username, final String password) {
        User user = new User();
        user.setEmailId(username);
        user.setPassword(Globals.encryptString(password));
        user.SignIn(getActivity(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                        JSONObject object = new JSONObject(result);
                        PrefUtils.saveToLoginPrefs(getActivity(), username, password);
                        JSONObject userProfileInfo = object.getJSONObject("userProfileInfo");
                        final JSONObject response = userProfileInfo.getJSONObject("response");
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
//                                realm.beginTransaction();
                                realm.createOrUpdateObjectFromJson(User.class, response.toString());
//                                realm.commitTransaction();
//                                realm.close();
                            }
                        });
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
            }
        });
    }
}
