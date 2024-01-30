package com.sp.my_iot_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TipsFragment extends Fragment {
    private TextView tipsTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        // Initialize the TextView
        tipsTextView = view.findViewById(R.id.tipsTextView);

        // Call the method to generate and display tips
        generateTips();

        return view;
    }


    private void generateTips() {
        SharedPreferences preferences = getActivity().getSharedPreferences("SensorData", Context.MODE_PRIVATE);

        // Retrieve stored sensor data
        float temperature = preferences.getFloat("temperature", 0.0f);
        float humidity = preferences.getFloat("humidity", 0.0f);
        int ldr = preferences.getInt("ldr", 0);

        // Implement your algorithm to generate tips based on sensor data
        String tips = generateTipsAlgorithm(temperature, humidity, ldr);

        // Set the generated tips in the TextView
        tipsTextView.setText(tips);
    }

    private String generateTipsAlgorithm(float temperature, float humidity, int ldr) {
        StringBuilder tipsBuilder = new StringBuilder();

        // Generate tips based on sensor data
        String temperatureTip = generateTemperatureTip(temperature);
        String humidityTip = generateHumidityTip(humidity);
        String ldrTip = generateLDRTip(ldr);

        // Append the tips to the StringBuilder
        tipsBuilder.append(temperatureTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(humidityTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(ldrTip);

        // Return the generated tips
        return tipsBuilder.toString();
    }

    private String generateTemperatureTip(float temperature) {
        // Implement logic to generate temperature tips
        // For example, you can have different tips for various temperature ranges
        String tip;
        if (temperature < 20) {
            tip = "It's quite cold. Consider wearing warm clothes.";
        } else if (temperature >= 20 && temperature < 30) {
            tip = "The temperature is moderate. Enjoy the pleasant weather.";
        } else {
            tip = "It's hot outside. Stay hydrated.";
        }
        return "Temperature Tip: " + tip;
    }

    private String generateHumidityTip(float humidity) {
        // Implement logic to generate humidity tips
        // For example, you can have different tips for various humidity levels
        String tip;
        if (humidity < 30) {
            tip = "Low humidity. Keep yourself hydrated.";
        } else if (humidity >= 30 && humidity < 60) {
            tip = "Moderate humidity. Enjoy the comfortable atmosphere.";
        } else {
            tip = "High humidity. Stay cool and use fans or air conditioning.";
        }
        return "Humidity Tip: " + tip;
    }

    private String generateLDRTip(int ldr) {
        // Implement logic to generate LDR tips
        // For example, you can have different tips for various light sensitivity levels
        String tip;
        if (ldr < 500) {
            tip = "Low light sensitivity. Consider turning on lights.";
        } else if (ldr >= 500 && ldr < 1000) {
            tip = "Moderate light sensitivity. Enjoy the natural light.";
        } else {
            tip = "High light sensitivity. Be mindful of glare and direct sunlight.";
        }
        return "LDR Tip: " + tip;
    }
}
