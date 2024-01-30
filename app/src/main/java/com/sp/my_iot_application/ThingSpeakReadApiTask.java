package com.sp.my_iot_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThingSpeakReadApiTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "ThingSpeakReadApiTask";
    private FragmentActivity activity;
    private TextView fieldTextView;
    private int fieldNumber;

    public ThingSpeakReadApiTask(FragmentActivity activity, TextView fieldTextView, int fieldNumber) {
        this.activity = activity;
        this.fieldTextView = fieldTextView;
        this.fieldNumber = fieldNumber;
    }

    @Override
    protected String doInBackground(String... params) {
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

            return response.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error reading data from ThingSpeak: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response != null) {
            // Update the TextView with the response
            int fieldValue = extractFieldValueFromResponse(response, fieldNumber);

            String fieldName;
            switch (fieldNumber) {
                case 1:
                    fieldName = "Temperature";
                    saveSensorData("temperature", fieldValue);
                    break;
                case 2:
                    fieldName = "Humidity";
                    saveSensorData("humidity", fieldValue);
                    break;
                case 6:
                    fieldName = "Environment Lighting";
                    saveSensorData("ldr", fieldValue);
                    break;
                default:
                    fieldName = "Field " + fieldNumber;
                    break;
            }

            fieldTextView.setText("Your most recent " + fieldName + " is: " + fieldValue);
        } else {
            // Handle the case where the response is null or there's an error
            fieldTextView.setText("Error fetching data from ThingSpeak");
        }

    }

    private void saveSensorData(String sensorType, float value) {
        SharedPreferences preferences = activity.getSharedPreferences("SensorData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(sensorType, value);
        editor.apply();
    }


    private int extractFieldValueFromResponse(String response, int fieldNumber) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);

            // Get the field value
            int fieldValue = jsonResponse.getInt("field" + fieldNumber);

            return fieldValue;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing ThingSpeak response: " + e.getMessage());
        }
        return 0;
    }
}
