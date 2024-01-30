package com.sp.my_iot_application;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ScheduledToastReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MyIOTChannel";
    private static final int NOTIFICATION_ID = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the stored field number and field value
        SharedPreferences preferences = context.getSharedPreferences("ScheduledData", Context.MODE_PRIVATE);
        int fieldNumber = preferences.getInt("fieldNumber", -1);
        int fieldValue = preferences.getInt("fieldValue", -1);

        // Display a toast message based on the field number and field value
        String device = (fieldNumber == 4) ? "Fan" : "Lamp";
        String status = (fieldValue == 1) ? "On" : "Off";

        String message = String.format("Scheduled API call: Device %s, Status %s", device, status);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        // Check the combination of device and status and create ThingSpeakApiTask
        if (fieldNumber != -1 && fieldValue != -1) {
            ThingSpeakApiTask thingSpeakApiTask = new ThingSpeakApiTask(fieldNumber, fieldValue);
            thingSpeakApiTask.execute();
        }
    }
}
