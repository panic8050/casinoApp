package com.zapps.slots.logic;

import android.content.Intent;
import android.graphics.Color;

import com.zapps.slots.R;
import com.daimajia.androidanimations.library.Techniques;

import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;

public class SplashScreen extends SplasherActivity {
    @Override
    public void initSplasher(SplasherConfig splasherConfig) {
        splasherConfig.setReveal_start(Const.START_CENTER) // anitmation type ..
                //---------------
                .setAnimationDuration(3000) // Reveal animation duration ..
                //---------------
                .setLogo(R.drawable.slots_logo) // logo src..
                .setLogo_animation(Techniques.BounceIn) // logo animation ..
                .setAnimationLogoDuration(2000) // logo animation duration ..
                .setLogoWidth(500) // logo width ..
                //---------------
                .setSubtitle(getResources().getString(R.string.app_name)) // subtitle
                .setSubtitleColor(Color.parseColor("#ffff4444")) // subtitle color
                .setSubtitleAnimation(Techniques.FadeIn) // subtitle animation (from Android View Animations) ..
                .setSubtitleSize(24); // subtitle font type ..
    }

    @Override
    public void onSplasherFinished() {
        Intent startMenu = new Intent(this, MenuActivity.class);
        startActivity(startMenu);
        finish();
    }
}
