package com.example.serviceinvoker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SSIDReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String ssid = intent.getStringExtra("SSID");
        Toast.makeText(context, "Received SSID: " + ssid, Toast.LENGTH_LONG).show();
    }
}
