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
        // Use the modified method to retrieve sensor data
        float temperature = getLastSensorValue("temperature");
        float humidity = getLastSensorValue("humidity");
        float ldr = getLastSensorValue("ldr");
        float motion = getLastSensorValue("motion");
        float fanStatus = getLastSensorValue("fanStatus");
        float lampStatus = getLastSensorValue("lampStatus");

        // Implement your algorithm to generate tips based on sensor data
        String tips = generateTipsAlgorithm(temperature, humidity, ldr, motion, fanStatus, lampStatus);

        // Set the generated tips in the TextView
        tipsTextView.setText(tips);
    }


    // Updated method to retrieve the last value from SharedPreferences
    private float getLastSensorValue(String sensorType) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("SensorData", Context.MODE_PRIVATE);
        return preferences.getFloat(sensorType, 0.0f); // Change 0 to the default value you want
    }


    private String generateTipsAlgorithm(float temperature, float humidity, float ldr, float motion, float fanStatus, float lampStatus) {
        StringBuilder tipsBuilder = new StringBuilder();

        // Generate tips based on sensor data
        String temperatureTip = generateTemperatureTip(temperature);
        String humidityTip = generateHumidityTip(humidity);
        String ldrTip = generateLDRTip(ldr);
        String motionTip = generateMotionTip(motion);
        String fanLampStatusTip = generateFanLampStatusTip(fanStatus, lampStatus);

        // Append the tips to the StringBuilder
        tipsBuilder.append(temperatureTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(humidityTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(ldrTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(motionTip);
        tipsBuilder.append("\n");
        tipsBuilder.append(fanLampStatusTip);

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

    private String generateLDRTip(float ldr) {
        // Implement logic to generate LDR tips
        // For example, you can have different tips for various light sensitivity levels
        String tip;
        if (ldr < 50) {
            tip = "Low light sensitivity. Consider turning on lights.";
        } else if (ldr >= 50 && ldr < 80) {
            tip = "Moderate light sensitivity. Enjoy the natural light.";
        } else {
            tip = "High light sensitivity. Be mindful of glare and direct sunlight.";
        }
        return "LDR Tip: " + tip;
    }
    private String generateMotionTip(float motion) {
        // Implement logic to generate tips based on motion detection
        String tip;
        if (motion == 1) {
            tip = "Motion detected. Ensure that the area is well-lit for better visibility.";
        } else {
            tip = "No motion detected. Save energy by turning off unnecessary lights and appliances.";
        }
        return "Motion Tip: " + tip;
    }

    private String generateFanLampStatusTip(float fanStatus, float lampStatus) {
        // Implement logic to generate tips based on fan and lamp status
        String tip;
        if (fanStatus == 1 && lampStatus == 0) {
            tip = "Fan is ON, but the lamp is OFF. Consider turning on the lamp for better illumination.";
        } else if (fanStatus == 0 && lampStatus == 1) {
            tip = "Lamp is ON, but the fan is OFF. If the room is warm, consider turning on the fan for ventilation.";
        } else if (fanStatus == 1 && lampStatus == 1) {
            tip = "Both Fan and Lamp are ON. Ensure you turn them off when not needed to save energy.";
        } else {
            tip = "Fan and Lamp are OFF. Save energy by keeping them off when not needed.";
        }
        return "Fan/Lamp Status Tip: " + tip;
    }
}
