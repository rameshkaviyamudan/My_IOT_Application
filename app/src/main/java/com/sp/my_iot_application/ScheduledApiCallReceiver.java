package com.sp.my_iot_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScheduledApiCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the selected device and status from the intent
        String selectedDevice = intent.getStringExtra("selectedDevice");
        String selectedStatus = intent.getStringExtra("selectedStatus");

        // Perform the Thingspeak API call here
        performApiCall(selectedDevice, selectedStatus);

        // Optionally, you can show a notification or perform other actions
    }

    private void performApiCall(String device, String status) {
        // Perform the Thingspeak API call with the provided device and status
        // Replace this with your actual API call logic
        // You might want to use a library like Retrofit for making API calls

        // Example: Send an HTTP request to Thingspeak
        String apiKey = "C7K4LI6SFXXPFSPJ";
        String apiUrl = "https://api.thingspeak.com/update?api_key=" + apiKey + "&field1=" + device + "&field2=" + status;

        // Perform the API call (you may use AsyncTask, Retrofit, etc.)
        // For simplicity, using a basic example with AsyncTask
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Execute the API call
                    URL url = new URL(apiUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    // Check the response code if needed
                    int responseCode = urlConnection.getResponseCode();
                    // Handle the response as needed

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
