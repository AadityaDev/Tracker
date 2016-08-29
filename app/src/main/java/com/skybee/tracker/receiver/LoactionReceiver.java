package com.skybee.tracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class LoactionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast Intent Detected.",
                Toast.LENGTH_LONG).show();
        Log.d("Receiver", "Location");
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                    Toast.LENGTH_SHORT).show();
//            Intent pushIntent = new Intent(context, LocalService.class);
//            context.startService(pushIntent);
        }

    }
}
