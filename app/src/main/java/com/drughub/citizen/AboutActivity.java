package com.drughub.citizen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseActivity  {

    private TextView appVersion,drughubWebsite;
    String appversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        setTitle(getString(R.string.about_info));
        setBackButton(true);
        appVersion = (TextView) findViewById(R.id.appVersion);
        drughubWebsite=(TextView)findViewById(R.id.drughubWebsite);
        drughubWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.drughub.in"));
                startActivity(intent);
            }
        });
        try {
            appversion=this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appVersion.setText(appversion);
    }

}
