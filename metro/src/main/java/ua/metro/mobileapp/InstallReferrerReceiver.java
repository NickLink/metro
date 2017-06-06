package ua.metro.mobileapp;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Log.v("METRo", "REFF" + referrer);
        //Use the referrer
        new CampaignTrackingReceiver().onReceive(context, intent);
    }
}