package com.finlandapps.musicejuicemp3;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.io.File;

public class SiskiAppMainActivity extends AppCompatActivity {

    SearchView searchview;
    Button offline_btn, policy, rate, share;
    ImageButton btn_search;
    LinearLayout banner;
    CardView cardHome;

    public static String DOWNLOAD_DIRECTORY="/Download";

    String search_query;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        searchview = findViewById(R.id.searchView);
        offline_btn = findViewById(R.id.btn_offline);
        cardHome = findViewById(R.id.cardHome);
        policy = findViewById(R.id.policy);
        rate = findViewById(R.id.rate);
        share = findViewById(R.id.share);
        banner = findViewById(R.id.banner);

        File theDir = new File(DOWNLOAD_DIRECTORY);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchview.setMaxWidth(Integer.MAX_VALUE);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_query = query;
                Intent i = new Intent(SiskiAppMainActivity.this, SiskiAppOnlineActivity.class);
                i.putExtra("query", search_query);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                search_query = query;
                return false;
            }
        });

       Ads ads = new Ads(SiskiAppMainActivity.this);
       ads.showBanner(banner);

        cardHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview.setIconified(false);
            }
        });

        offline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SiskiAppMainActivity.this, SiskiAppOfflineActivity.class);
                startActivity(i);
            }
        });

//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (search_query != null) {
//                    Intent i = new Intent(SiskiAppMainActivity.this, SiskiAppOnlineActivity.class);
//                    i.putExtra("query", search_query);
//                    startActivity(i);
//                } else {
//                    Toast.makeText(SiskiAppMainActivity.this, "Please fill your keyword. ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SiskiAppMainActivity.this, SiskiAppPrivacyActivity.class);
                startActivity(i);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getPackageName())));
                }
            }
        });


    }

    public void Exit() {
        new AlertDialog.Builder(SiskiAppMainActivity.this)
                .setTitle("Are you sure to exit.?")
                .setMessage("Please take your time to support us by rating and leave a review on PlayStore. Thanks.")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Rate Apps", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id="
                                            + getPackageName())));
                        }
                    }
                })
                .setNegativeButton("Remind  Me Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(1);
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_BACK && !searchview.isIconified()) {
            searchview.setIconified(true);
            searchview.setIconified(true);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            Exit();
            return false;
        }

        return false;
    }
}
