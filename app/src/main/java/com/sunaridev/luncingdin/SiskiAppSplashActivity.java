package com.sunaridev.luncingdin;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SiskiAppSplashActivity extends AppCompatActivity {

    ImageView splashImg;
    Button playbtn;
    TextView splashTitle;
    ProgressBar splashProgress;
    Animation anim;
    SharedPreference sharedPreference;

    static String PACKAGE_NAME;
    public static String id_inter;
    public static String id_banner;
    public static String ads_sett = "";
    public static String availability;
    public static String sc = "";
    static String moving_link;
    static String json;
    public static String folder;
    public static boolean online;

    Handler handler = new Handler();
    static int PERCODE = 1212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        createInterstitial();

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

        splashImg = findViewById(R.id.splashImg);
        playbtn = findViewById(R.id.splashButton);

        anim = AnimationUtils.loadAnimation(SiskiAppSplashActivity.this, R.anim.fadein);

        folder = Environment.getExternalStorageDirectory() + File.separator + "Download";
        sharedPreference = new SharedPreference(this);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1111);
            }
        }

        if (CheckConnection()) {
            if (sharedPreference.getApp_runFirst().equals("FIRST")) {
                showPolicy();
            }

            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStatusApp(getString(R.string.json));

                }
            });
        }
        else warningpolicy();
    }



    private void getStatusApp(String url){
        Log.e("url", url );
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response1 -> {
            try {
                JSONArray response2= response1.getJSONArray("paramapps");
                JSONObject response= response2.getJSONObject(0);
                Ads.statusapp=response.getString("avail");
                Ads.appupdate=response.getString("packagename");
                Ads.admobads=response.getString("ads_sett");
                Ads.applovinbanner=response.getString("applovinbanner");
                Ads.applovininter=response.getString("applovininter");
                Ads.admobbanner=response.getString("banneradmob");
                Ads.admobinter=response.getString("interadmob");
                sc=response.getString("sckey");

                if (!Ads.statusapp.equals("y")){
                    showDialog(Ads.appupdate);
                }
                else {

                    Ads ads = new Ads(SiskiAppSplashActivity.this);
                    ads.showinters();
                    ads.setadsListener(new Ads.adsListener() {
                        @Override
                        public void onAdsfinish() {
                            Intent intent = new Intent(SiskiAppSplashActivity.this,SiskiAppMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });


                }
            } catch (JSONException e) {
                Log.e("errorparsing",e.getMessage());
            }
        }, error -> {
            Log.e("url", error.getMessage() );

        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    private void  showDialog(String appupdate){
        new SweetAlertDialog(SiskiAppSplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("App Was Discontinue")
                .setContentText("Please Install Our New Music App")
                .setConfirmText("Install")

                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Install From Playstore")
                            .setContentText("Please Wait, Open Playstore")
                            .setConfirmText("Go")


                            .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(
                                "https://play.google.com/store/apps/details?id="+appupdate));
                        intent.setPackage("com.android.vending");
                        startActivity(intent);
//                                Do something after 100ms
                    }, 3000);



                })
                .show();
    }














    public boolean CheckConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



    public void showPolicy(){
        String txt;
        try {
            InputStream is = getAssets().open("policy.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            txt = new String(buffer);
        } catch (IOException ex) {
            return;
        }

        AlertDialog alertDialogPolicy = new AlertDialog.Builder(SiskiAppSplashActivity.this).create();
        alertDialogPolicy.setTitle("Privacy Policy");
        alertDialogPolicy.setIcon(R.mipmap.ic_launcher);
        alertDialogPolicy.setMessage(txt);
        alertDialogPolicy.setButton(AlertDialog.BUTTON_POSITIVE, "Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sharedPreference.setApp_runFirst("NO");
                        Ads ads = new Ads(SiskiAppSplashActivity.this);
                        ads.showinters();
                        ads.setadsListener(new Ads.adsListener() {
                            @Override
                            public void onAdsfinish() {
                                Intent intent = new Intent(SiskiAppSplashActivity.this,SiskiAppMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                });
        alertDialogPolicy.setButton(AlertDialog.BUTTON_NEGATIVE, "Decline",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        warningpolicy();

                    }
                });
        alertDialogPolicy.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                warningpolicy();
            }
        });
        alertDialogPolicy.show();
    }

    public void warningpolicy() {
        if (sharedPreference.getApp_runFirst().equals("FIRST")) {
            AlertDialog alertDialogPolicy = new AlertDialog.Builder(SiskiAppSplashActivity.this).create();
            alertDialogPolicy.setTitle("Policy Warning !");
            alertDialogPolicy.setIcon(R.mipmap.ic_launcher);
            alertDialogPolicy.setMessage("Please accept our policy before use this App.\nThank You.");
            alertDialogPolicy.setButton(AlertDialog.BUTTON_POSITIVE, "Restart",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });
            alertDialogPolicy.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            System.exit(1);
                            finish();
                        }
                    });
            alertDialogPolicy.show();
        }
    }
    public void btn_rate(){
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
    public void MoveApps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(SiskiAppSplashActivity.this)
                        .setTitle("Apps Maintenance")
                        .setMessage("This App is on maintenance,\nPlease go to our new apps with new feature and new experience.")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable( false )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id="
                                                + moving_link)));
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    public void WarningBox() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(SiskiAppSplashActivity.this)
                        .setTitle("No Connection")
                        .setMessage("Please check your internet connection\nThis app running well with good connection")
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(1);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
}
