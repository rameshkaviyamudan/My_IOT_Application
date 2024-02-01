package com.sp.my_iot_application;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThingSpeakReadApiTask extends AsyncTask<String, Void, Integer> {

    private static final String TAG = "ThingSpeakReadApiTask";
    private ThingSpeakReadCallback callback;
    private int fieldNumber;

    public ThingSpeakReadApiTask(int fieldNumber, ThingSpeakReadCallback callback) {
        this.fieldNumber = fieldNumber;
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            // Construct the URL
            URL url = new URL(params[0]);

            // Open connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Disconnect
            urlConnection.disconnect();

            return extractFieldValueFromResponse(response.toString(), fieldNumber);
        } catch (IOException e) {
            Log.e(TAG, "Error reading data from ThingSpeak: " + e.getMessage());
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer fieldValue) {
        super.onPostExecute(fieldValue);

        // Notify the callback with the result
        if (callback != null) {
            callback.onThingSpeakDataReceived(fieldNumber, fieldValue);
        }
        // No UI-related code here
    }

    private int extractFieldValueFromResponse(String response, int fieldNumber) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);

            // Get the field value
            return jsonResponse.getInt("field" + fieldNumber);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing ThingSpeak response: " + e.getMessage());
        }
        return 0;
    }
}
