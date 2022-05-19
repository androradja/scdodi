package com.finlandapps.musicejuicemp3;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kaopiz.kprogresshud.KProgressHUD;


import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

import java.util.Arrays;


public class Ads {


    public static  String statusapp="";
    public static String appupdate = "";
    public static String applovinbanner="";
    public static String applovininter="";
    public static String admobbanner="";
    public static String admobinter="";
    public static String admobads="";
    public static String primaryads="";

    private InterstitialAd admobInterstitialAd;
    private AdView admobview;

    public  static   String appid="";
    Activity activity;


    KProgressHUD hud;

    public interface adsListener {
        void onAdsfinish();
    }

    private adsListener listener;

    public void setadsListener(adsListener listener) {
        this.listener = listener;
    }

    public Ads(Activity activity) {
        this.activity = activity;

        AppLovinSdk.getInstance( activity ).setMediationProvider( "max" );
//        AppLovinSdk.getInstance( activity ).showMediationDebugger();
        AppLovinSdk.getInstance(activity).getSettings().setTestDeviceAdvertisingIds(Arrays.asList("80784a92-8b3a-47c6-8de5-e290aa8db75c","5bc74b6a-89f9-48b5-ab81-3c5a1eeab1f1"));

        AppLovinSdk.initializeSdk( activity, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads

            }
        } );


        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }


    public  void  showinters(){
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(true)
                .setDetailsLabel("Please Wait")
                .setMaxProgress(100)
                .show();
        if (primaryads.equals("admob")){
            showinterAdmob();

        }
        else {
            showinterMax();

        }
    }

    public void showBanner(LinearLayout mAdViewLayout){
        if (primaryads.equals("admob")){
            showBannerAdmob(mAdViewLayout);
        }
        else {
            showBannerMax(mAdViewLayout);
        }

    }


    public void showinterAdmob(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity,admobinter, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        hud.dismiss();
                        admobInterstitialAd=interstitialAd;
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.

                        admobInterstitialAd.show(activity);
                        admobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                listener.onAdsfinish();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                listener.onAdsfinish();
                            }
                        });

                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        listener.onAdsfinish();
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                    }
                });



    }



    public void showBannerAdmob(LinearLayout mAdViewLayout){
        mAdViewLayout.removeAllViews();
        admobview = new AdView(activity);
        admobview.setAdSize(AdSize.FULL_BANNER);
        admobview.setAdUnitId(admobbanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        admobview.loadAd(adRequest);
        mAdViewLayout.addView(admobview);




    }

    public void showinterMax(){

        MaxInterstitialAd interstitialAd;
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(true)
                .setDetailsLabel("Please Wait")
                .setMaxProgress(100)
                .show();
        interstitialAd = new MaxInterstitialAd( applovininter, activity );
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                interstitialAd.showAd();
                Log.e("MaxInterstitialAd", "onAdLoaded: ");


            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                hud.dismiss();
//               listener.onAdsfinish();
                Log.e("MaxInterstitialAd", "onAdDisplayed: ");


            }

            @Override
            public void onAdHidden(MaxAd ad) {
                hud.dismiss();
                listener.onAdsfinish();
                Log.e("MaxInterstitialAd", "onAdHidden: ");

            }

            @Override
            public void onAdClicked(MaxAd ad) {
                hud.dismiss();
                listener.onAdsfinish();
                Log.e("MaxInterstitialAd", "onAdClicked: ");


            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                hud.dismiss();
                listener.onAdsfinish();
                Log.e("MaxInterstitialAd", "onAdLoadFailed: ");


            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.e("MaxInterstitialAd", "onAdDisplayFailed: ");

                hud.dismiss();
                listener.onAdsfinish();


            }
        });

        // Load the first ad
        interstitialAd.loadAd();



    }





    public  void showBannerMax(LinearLayout mAdViewLayout) {
        MaxAdView adView;
        adView = new MaxAdView( applovinbanner, activity );

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // Get the adaptive banner height.
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( activity ).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx( activity, heightDp );

        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        adView.setExtraParameter( "adaptive_banner", "true" );
        mAdViewLayout.addView(adView);
        // Load the ad
        adView.loadAd();




    }


}