package com.example.servicecreator;

import static android.os.Binder.getCallingUid;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class WifiService extends Service {
    public static final String ACTION_WIFI_SSID = "com.example.servicecreator.WIFI_SSID";
    private static final String CUSTOM_PERMISSION = "com.example.servicecreator.CUSTOM_PERMISSION";
    private static final String TAG = "WifiService";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "WiFi Service started!");

        // Verify calling package signature
        if (!isCallerSignedWithSameKey()) {
            Log.e(TAG, "SIGNATURE MISMATCH: ServiceInvoker is not signed with the same key as ServiceCreator!");
            return START_NOT_STICKY;
        }

        // Check if the calling package has the custom permission
        if (checkCallingOrSelfPermission(CUSTOM_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission granted! Proceeding to fetch SSID.");
            sendWifiSSID();
        } else {
            Log.e(TAG, "MISSING PERMISSION: ServiceInvoker does not have the required custom permission!");
        }

        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private boolean isCallerSignedWithSameKey() {
        PackageManager pm = getPackageManager();
        String callerPackage = getPackageName(); // Default to this app itself

        try {
            int callingUid = getCallingUid();
            String[] callingPackages = pm.getPackagesForUid(callingUid);
            if (callingPackages != null && callingPackages.length > 0) {
                callerPackage = callingPackages[0];
            }

            PackageInfo callerInfo = pm.getPackageInfo(callerPackage, PackageManager.GET_SIGNING_CERTIFICATES);
            PackageInfo myInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);

            if (callerInfo.signingInfo.hasMultipleSigners() || myInfo.signingInfo.hasMultipleSigners()) {
                return false; // Ensure single key signing
            }

            return callerInfo.signingInfo.getSigningCertificateHistory()[0]
                    .equals(myInfo.signingInfo.getSigningCertificateHistory()[0]);
        } catch (Exception e) {
            Log.e(TAG, "Error verifying signature", e);
            return false;
        }
    }

    private void sendWifiSSID() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();

            if (ssid == null || ssid.equals("<unknown ssid>")) {
                Log.w(TAG, "SSID is unknown. Possible causes: No location permission, not connected to WiFi, or Android restrictions.");
                ssid = "SSID not available";
            }

            Intent broadcastIntent = new Intent(ACTION_WIFI_SSID);
            broadcastIntent.putExtra("SSID", ssid);
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "Broadcasting SSID: " + ssid);
        } else {
            Log.e(TAG, "WifiManager is NULL!");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
