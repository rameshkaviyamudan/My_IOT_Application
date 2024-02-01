package com.sp.my_iot_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class TipsFragment extends Fragment {
    private LinearLayout tipsContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        // Initialize the TextView
        tipsContainer = view.findViewById(R.id.tipsContainer);

        // Call the method to generate and display tips
        generateTips();

        return view;
    }

    private void generateTips() {
        float temperature = getLastSensorValue(1);
        float humidity = getLastSensorValue(2);
        float ldr = getLastSensorValue(6);
        float motion = getLastSensorValue(3);
        float fanStatus = getLastSensorValue(4);
        float lampStatus = getLastSensorValue(5);

        addTipCard("Temperature Tip", generateTemperatureTip(temperature), Color.parseColor("#FFE0B2")); // Light Orange
        addTipCard("Humidity Tip", generateHumidityTip(humidity), Color.parseColor("#C8E6C9")); // Light Green
        addTipCard("LDR Tip", generateLDRTip(ldr), Color.parseColor("#D1C4E9")); // Light Purple
        addTipCard("Motion Tip", generateMotionTip(motion), Color.parseColor("#FFE0B2")); // Light Orange
        addTipCard("Fan/Lamp Status Tip", generateFanLampStatusTip(fanStatus, lampStatus), Color.parseColor("#C8E6C9")); // Light Green

    }
    private void addTipCard(String title, String tip, int cardColor) {
        // Create a new CardView
        CardView cardView = new CardView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 40); // Add margin for spacing between cards
        cardView.setLayoutParams(layoutParams);
        cardView.setCardBackgroundColor(cardColor); // Set the background color of the card
        cardView.setCardElevation(4); // Customize the elevation as needed

        // Create a new TextView for the tip content
        TextView tipTextView = new TextView(requireContext());
        tipTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        tipTextView.setPadding(16, 16, 16, 16); // Add padding for better appearance
        tipTextView.setText(title + "\n\n" + tip);

        // Add the TextView to the CardView
        cardView.addView(tipTextView);

        // Add the CardView to the tipsContainer
        tipsContainer.addView(cardView);
    }






    // Updated method to retrieve the last value from SharedPreferences
    private float getLastSensorValue(int fieldNumber) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("SensorData", Context.MODE_PRIVATE);

        // Adjust the field number if it is 6
        int adjustedFieldNumber = (fieldNumber == 3) ? 6 : fieldNumber;

        switch (adjustedFieldNumber) {
            case 1:
                return preferences.getInt("field_1", 0);
            case 2:
                return preferences.getInt("field_2", 0);
            case 3:
                return preferences.getInt("field_3", 0);
            case 4:
                return preferences.getInt("field_4", 0);
            case 5:
                return preferences.getInt("field_5", 0);
            case 6:
                return preferences.getInt("field_6", 0);
            default:
                return 0;
        }
    }



    private String generateTipsAlgorithm(float temperature, float humidity, float ldr, float motion, float fanStatus, float lampStatus) {
        StringBuilder tipsBuilder = new StringBuilder();

        // Generate tips based on sensor data
        String temperatureTip = generateTemperatureTip(temperature);
        String humidityTip = generateHumidityTip(humidity);
        String ldrTip = generateLDRTip(ldr);
        String motionTip = generateMotionTip(motion);
        String fanLampStatusTip = generateFanLampStatusTip(fanStatus, lampStatus);

        // Append the tips to the StringBuilder with newline characters for spacing
        tipsBuilder.append(temperatureTip).append("\n\n");
        tipsBuilder.append(humidityTip).append("\n\n");
        tipsBuilder.append(ldrTip).append("\n\n");
        tipsBuilder.append(motionTip).append("\n\n");
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
