package com.drughub.citizen.model;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.drughub.citizen.network.Globals;
import com.drughub.citizen.network.Urls;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
// {"firstName":"doctor","lastName":"Last","userProfileId":58,
// "partnerNature":"Service Providers","mobile":"9966998899","emailId":"doctor111@drughub.in",
// "middleName":"Middle","gender":{"value":"Male","id":1},"profileProfileId":null,"role":null,"password":"a642a77abd7d4f51bf9226ceaf891fcbb5b299b8","userId":81}

    private String firstName;
    private String middleName;
    private String lastName;
    @PrimaryKey
    private int userProfileId;
    private String mobile;
    private String emailId;
    private String userId;
    private String password;
    private String name;


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String toSignUp() {
        JSONObject params = new JSONObject();
        try {
            params.put("name", getName());
            params.put("email", getEmailId());
            params.put("mobile", getMobile());
            params.put("password", getPassword());
//            object.put("category", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params.toString();
    }

    public String toSignIn() {
        JSONObject object = new JSONObject();
        try {
            object.put("email", getEmailId());
            object.put("passwordDigest", getPassword());
            object.put("typeOfLogin", "Android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String toForgetPassword() {
        JSONObject object = new JSONObject();
        try {
            object.put("email", getEmailId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }


    public void SignUp(Context context, final Globals.VolleyCallback callback) {

        Globals.POST(Urls.SIGN_UP, toSignUp(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
//                    Log.v("response"," "+result );
                    callback.onSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "Signing Up");
    }

    public void SignIn(Context context, final Globals.VolleyCallback callback) {

        Globals.POST(Urls.SIGN_IN, toSignIn(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "Signing In");
    }

    public void ForgetPassword(FragmentActivity activity, final Globals.VolleyCallback callback) {

        Globals.POST(Urls.FORGET_PASSWORD, toForgetPassword(), new Globals.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);
            }
        }, "");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
