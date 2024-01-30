package com.sp.my_iot_application;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThingSpeakApiTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "ThingSpeakApiTask";

    // ThingSpeak API URL
    private static final String API_URL = "https://api.thingspeak.com/update";

    // Replace with your ThingSpeak API key
    private static final String API_KEY = "C7K4LI6SFXXPFSPJ";

    private int fieldNumber;
    private int fieldValue;

    public ThingSpeakApiTask(int fieldNumber, int fieldValue) {
        this.fieldNumber = fieldNumber;
        this.fieldValue = fieldValue;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Construct the URL with API key
            String apiUrl = String.format("%s?api_key=%s", API_URL, API_KEY);

            // Open connection
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // Write data to the request body
            String data = String.format("field%d=%d", fieldNumber, fieldValue);
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            // Get the response code (optional)
            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            // Disconnect
            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e(TAG, "Error sending data to ThingSpeak: " + e.getMessage());
        }

        return null;
    }

}
