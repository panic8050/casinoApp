package com.mapplic.fortune;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.mapplic.fortune.logic.MainActivity;

public class SplashActivity extends Activity {
    private static int WELCOME_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       initSplashActivity();
    }

    public  void initSplashActivity() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            Intent welcomeIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(welcomeIntent);

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            finish();
        }, WELCOME_TIMEOUT);
    }

}
