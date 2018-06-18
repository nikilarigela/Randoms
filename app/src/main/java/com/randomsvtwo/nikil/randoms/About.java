package com.randomsvtwo.nikil.randoms;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {
    TextView versionText;
    String versionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        versionText = (TextView)findViewById(R.id.version);
        try {
            PackageInfo pckinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionString = pckinfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionText.setText(versionString);


    }
}
