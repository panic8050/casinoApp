package com.zapps.slots.logic;

import android.content.Intent;
import android.graphics.Color;

import com.zapps.slots.R;
import com.daimajia.androidanimations.library.Techniques;

import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;

public class SplashAct extends SplasherActivity {

    @Override
    public void initSplasher(SplasherConfig splasherConfig) {
        splasherConfig.setReveal_start(Const.START_CENTER)
                .setAnimationDuration(3000)
                .setLogo(R.drawable.logo)
                .setLogo_animation(Techniques.BounceIn)
                .setAnimationLogoDuration(2000)
                .setLogoWidth(500)
                .setSubtitle(getResources().getString(R.string.app_name))
                .setSubtitleColor(Color.parseColor("#ffff4444"))
                .setSubtitleAnimation(Techniques.FadeIn)
                .setSubtitleSize(24);
    }

    @Override
    public void onSplasherFinished() {
        Intent startMenu = new Intent(this, MenuActivity.class);
        startActivity(startMenu);
        finish();
    }
}
