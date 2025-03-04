package com.example.serviceinvoker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ServiceInvoker";
    private BroadcastReceiver wifiReceiver;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button invokeService = findViewById(R.id.invokeService);
        invokeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Invoking WiFi Service...");
                Intent serviceIntent = new Intent("com.example.servicecreator.WIFI_SERVICE");
                serviceIntent.setPackage("com.example.servicecreator");
                startService(serviceIntent);
            }
        });

        wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ssid = intent.getStringExtra("SSID");
                if (ssid != null) {
                    Log.d(TAG, "Received SSID: " + ssid);
                    Toast.makeText(context, "Connected to: " + ssid, Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "SSID data missing from broadcast!");
                }
            }
        };

        IntentFilter filter = new IntentFilter("com.example.servicecreator.WIFI_SSID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(wifiReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(wifiReceiver, filter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }
}
