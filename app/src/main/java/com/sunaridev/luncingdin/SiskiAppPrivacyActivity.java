package com.sunaridev.luncingdin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class SiskiAppPrivacyActivity extends AppCompatActivity {

    LinearLayout banner;
    TextView textprivacy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        banner = findViewById(R.id.banner);
        textprivacy = findViewById(R.id.textpolicy);

       new Ads(SiskiAppPrivacyActivity.this).showBanner(banner);

        try {
            InputStream is = getAssets().open("policy.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String txt = new String(buffer);
            textprivacy.setText(txt);
        } catch (IOException ex) {
            return;
        }
    }
}
