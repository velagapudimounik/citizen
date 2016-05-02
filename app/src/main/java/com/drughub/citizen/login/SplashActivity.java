package com.drughub.citizen.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.drughub.citizen.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_WAIT_TIME = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new SplasTask().execute();

    }

    public class SplasTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(SPLASH_WAIT_TIME);
            } catch (InterruptedException e) {
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }

        @Override
        protected void onCancelled() {

        }
    }
}
