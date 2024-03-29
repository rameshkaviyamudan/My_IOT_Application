package com.sp.my_iot_application;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
// Use the appropriate import statements
import pl.droidsonroids.gif.GifImageView;
public class MainActivity extends AppCompatActivity implements ThingSpeakReadCallback {
    // This method is called when ThingSpeakReadApiTask completes its execution
    @Override
    public void onThingSpeakDataReceived(int fieldNumber, int fieldValue) {
        // Store the data in SharedPreferences for each field
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getFieldKey(fieldNumber), fieldValue);
        editor.apply();

        // Display a Toast indicating successful storage
        String message = String.format("Field %d value successfully stored: %d", fieldNumber, fieldValue);
        showToast(message);

        // Check if the received data is for Field 1 (temperature)
        // Check if the received data is for Field 1 (temperature)
        if (fieldNumber == 1) {
            CircularColorView circularColorView = findViewById(R.id.circularColorView);
            circularColorView.setTemperature(fieldValue);
            updateTemperatureUI(fieldValue);
        }

        if (fieldNumber == 4) {
            Button fanButton = findViewById(R.id.fanButton);
            boolean isFanOn = fieldValue == 1;
            fanButton.setActivated(isFanOn);

            int imageResource = isFanOn ? R.drawable.ic_button_on : R.drawable.ic_button_off;
            fanButton.setBackgroundResource(imageResource);

            updateFanButtonUI(isFanOn);

            // Make the API call immediately when the state changes
            int fanFieldNumber = 4;
            int fanFieldValue = isFanOn ? 1 : 0;
            makeApiCall(fanFieldNumber, fanFieldValue);
        }

        if (fieldNumber == 5) {
            Button lampButton = findViewById(R.id.lampButton);
            boolean isLampOn = fieldValue == 1;
            lampButton.setActivated(isLampOn);

            int imageResource = isLampOn ? R.drawable.ic_button_on : R.drawable.ic_button_off;
            lampButton.setBackgroundResource(imageResource);

            updateLampButtonUI(isLampOn);
        }




    }


    private LinearLayout pullDownMenu;
    private ImageView arrowIcon;
    private TextView temptext;

    private TextView temperatureTextView;
    private ThingSpeakReadApiTask readApiTask; // Add this line
    private String readApiUrl; // Declare readApiUrl

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "SensorData";

    private BottomNavigationView bottomNavigationView;
    public static final String CHANNEL_ID = "MyIOTChannel";
    private static final int NOTIFICATION_REQUEST_CODE = 1001;
    private String  channelId="2348974";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        // Initialize views
        arrowIcon = findViewById(R.id.arrowIcon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Assuming you have a GifImageView for fan and lamp icons
        GifImageView fanIcon = findViewById(R.id.fanIcon);
        GifImageView lampIcon = findViewById(R.id.lampIcon);
// Construct the URL for reading data from Field 1
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Call the ThingSpeak API to read data for Field 1
        readThingSpeakData();
      //  }


// Check if notification permission is granted
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            showNotificationPermissionDialog();
        }
        // Assuming you have a Button for fan and lamp buttons
        Button fanButton = findViewById(R.id.fanButton);
        Button lampButton = findViewById(R.id.lampButton);

        fanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the state
                boolean isChecked = !fanButton.isActivated();
                fanButton.setActivated(isChecked);

                // Change the background resource based on the toggle state
                int imageResource = isChecked ? R.drawable.ic_button_on : R.drawable.ic_button_off;
                fanButton.setBackgroundResource(imageResource);

                // Update the fan button UI
                updateFanButtonUI(isChecked);

                // Make the API call immediately when the state changes
                int fieldNumber = 4;
                int fieldValue = isChecked ? 1 : 0;
                makeApiCall(fieldNumber, fieldValue);
            }
        });

        lampButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the state
                boolean isChecked = !lampButton.isActivated();
                lampButton.setActivated(isChecked);

                // Change the background resource based on the toggle state
                int imageResource = isChecked ? R.drawable.ic_button_on : R.drawable.ic_button_off;
                lampButton.setBackgroundResource(imageResource);

                // Update the lamp button UI
                updateLampButtonUI(isChecked);

                // Make the API call immediately when the state changes
                int fieldNumber = 5;
                int fieldValue = isChecked ? 1 : 0;
                makeApiCall(fieldNumber, fieldValue);
            }
        });



        // Set onClickListener for the arrow icon
        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePullDownMenu();
            }
        });

        // Set up the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle bottom navigation item clicks here
                        if (item.getItemId() == R.id.action_home) {
                            // Handle Home
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            // Finish the current activity
                            finish();
                            return true;
                        } else if (item.getItemId() == R.id.action_tips) {
                            // Handle Tips
                            loadFragment(new TipsFragment());
                            return true;
                        } else if (item.getItemId() == R.id.action_dashboard) {
                            // Handle Monitoring
                            //openPullDownMenuFragmentTest();
                            loadFragment(new DashboardFragment());

                            return true;
                        }
                        return false;
                    }
                });

        // Call the ThingSpeak API to read data from Field 4
        //loadFragment(new DashboardFragment());


    }
    // Function to open PullDownMenuFragmentTest
    private void openPullDownMenuFragmentTest() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the existing fragment with PullDownMenuFragmentTest
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to toggle the visibility of the pull-down menu with animation
    private void togglePullDownMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PullDownMenuFragment pullDownMenuFragment = (PullDownMenuFragment) fragmentManager.findFragmentByTag("pullDownMenu");
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        ImageView arrowIcon = findViewById(R.id.arrowIcon);
        if (pullDownMenuFragment == null) {
            // If menu is not visible, show it
            pullDownMenuFragment = new PullDownMenuFragment();
            fragmentTransaction.add(R.id.fragment_container, pullDownMenuFragment, "pullDownMenu");
            // Grey out the background
            findViewById(R.id.main_layout).setAlpha(0.5f);
            // Adjust the translation to include arrow's height
            fragmentContainer.animate().translationY(arrowIcon.getHeight());
            arrowIcon.setRotation(180); // Rotate arrow icon
        } else {
            // If menu is visible, hide it
            fragmentTransaction.remove(pullDownMenuFragment);

            // Reset the translation and rotation
            fragmentContainer.animate().translationY(0);
            arrowIcon.setRotation(0);
            // Remove the greyed-out background
            findViewById(R.id.main_layout).setAlpha(1.0f);
        }

        fragmentTransaction.commit();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My IOT Channel";
            String description = "Channel for My IOT Application";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotificationPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification Permission");
        builder.setMessage("Please enable notifications to use this app.");
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestNotificationPermission();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // or handle the exit action as needed
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestNotificationPermission() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivityForResult(intent, NOTIFICATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NOTIFICATION_REQUEST_CODE) {
            // Check notification permission after the user has taken action
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                // User still hasn't enabled notifications, handle accordingly
                finish(); // or display another message or take appropriate action
            }
        }
    }

    private void makeApiCall(int fieldNumber, int fieldValue) {
        // Replace the following line with your actual API call logic
        // Use fieldNumber and fieldValue in the API call parameters
        // Example: ApiClient.sendDataToThingspeak(fieldNumber, fieldValue);
        ThingSpeakApiTask thingSpeakApiTask = new ThingSpeakApiTask(fieldNumber, fieldValue);
        thingSpeakApiTask.execute();

        // For testing purposes, display a Toast message
        String message = String.format("API call: Device %d, Status %d", fieldNumber, fieldValue);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // This method is called when ThingSpeakReadApiTask completes its execution
    // Method to read data from ThingSpeak for a specific field
    private void readThingSpeakData() {
        // Replace with your ThingSpeak Read API key and channel ID
        String apiKey = "HYIS4JRXOUDEEVC1";
        String channelId = "2348974"; // Replace with your actual ThingSpeak channel ID

        for (int i = 1; i <= 6; i++) {
            // Construct the URL for reading data from the specified field
            String readApiUrl = String.format("https://api.thingspeak.com/channels/%s/fields/%d/last.json?api_key=%s", channelId, i, apiKey);

            // Call ThingSpeakReadApiTask for each field
            ThingSpeakReadApiTask readApiTask = new ThingSpeakReadApiTask(i, this);
            readApiTask.execute(readApiUrl);
        }
    }

    private String getFieldKey(int fieldNumber) {
        // Generate a key for storing the field value in SharedPreferences
        return "field_" + fieldNumber;
    }
    // Helper method to display a Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    // Method to update the temperature TextView
    private void updateTemperatureUI(int temperature) {
        // Update the temperature TextView inside the circle
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        temperatureTextView.setText(String.format("%d°C", temperature));
    }


    private int getBackgroundColorForTemperature(int temperature) {
        // Define temperature ranges
        int coolTemp = 20;
        int moderateTemp = 30;
        int hotTemp = 40;

        if (temperature <= coolTemp) {
            return R.color.coolColor; // Cool Blue
        } else if (temperature <= moderateTemp) {
            return R.color.moderateColor; // Light Blue
        } else if (temperature <= hotTemp) {
            return R.color.hotColor; // Orange
        } else {
            return R.color.extremeHotColor; // Red
        }
    }
    private void updateFanButtonUI(boolean isFanOn) {
        int imageResource = isFanOn ? R.drawable.ic_fan_on_anim : R.drawable.ic_fan_off;
        Glide.with(MainActivity.this)
                .asGif()
                .load(imageResource)
                .into((GifImageView) findViewById(R.id.fanIcon));
    }

    private void updateLampButtonUI(boolean isLampOn) {
        int imageResource = isLampOn ? R.drawable.ic_lamp_on_anim : R.drawable.ic_lamp_off;
        Glide.with(MainActivity.this)
                .asGif()
                .load(imageResource)
                .into((GifImageView) findViewById(R.id.lampIcon));
    }

}

