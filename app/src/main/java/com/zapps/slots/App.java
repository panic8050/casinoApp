package com.zapps.slots;

import android.app.Application;

import com.onesignal.OneSignal;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        setAutoLogAppEventsEnabled(true);
    }
}
