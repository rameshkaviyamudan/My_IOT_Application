# IoT Home Automation System with Android App

This project aims to create an IoT-based home automation system controlled via an Android application. The system consists of an Arduino Uno board equipped with various sensors and actuators, which communicate with the Android app and ThingSpeak platform for data exchange and control.

## Features

1. **Sensor Integration**: The Arduino Uno board is connected to sensors including temperature, humidity, LDR, and IR sensors to collect environmental data.

2. **Actuator Control**: The Arduino Uno also controls actuators such as a fan and lamp based on user commands received from the Android app.

3. **ThingSpeak Integration**: Sensor data is periodically uploaded to the ThingSpeak platform. The status of the fan and lamp is updated in real-time on ThingSpeak based on user interactions with the Android app.

4. **Android Application**: The Android application provides users with a user-friendly interface to monitor sensor data, control actuators, and schedule automation tasks.

5. **Scheduling**: Users can schedule automation tasks using Android's notification channel. The app sends scheduled commands to ThingSpeak to control the fan and lamp.

## Implementation Details

- **Data Upload Frequency**: Sensor data is uploaded to ThingSpeak each time the loop restarts on the Arduino Uno board.

- **Button Functionality**: Pressing buttons on the Arduino Uno board toggles the fan and lamp directly and sends their updated status to ThingSpeak. The Android app periodically refreshes the status from ThingSpeak to reflect the latest changes.

- **Fan and Lamp Status Checking**: The Android app checks the status of the fan and lamp fields on ThingSpeak each time the loop restarts.

- **Notification Channel**: Scheduled tasks are implemented using Android's notification channel and alarm permissions. Users can schedule commands for fan and lamp control, which are executed in the background.

- **User Interface**: The Android app features a home page with temperature display and fan/lamp control buttons. Users can navigate to a dashboard to view sensor data graphs and schedule automation tasks.

## User Guide

1. **Installation**: Install the Android app on your device and ensure the Arduino Uno board is connected and configured.

2. **Usage**: Open the Android app and navigate to the home page. Use the fan and lamp control buttons to toggle their status. Schedule automation tasks via the scheduling menu in the navigation drawer.

3. **Monitoring**: View real-time sensor data and graphs on the dashboard page. Swipe left or right to switch between different sensor graphs.

4. **Scheduling**: Set up automation tasks using the scheduling feature. Choose the device (fan/lamp), action (on/off), time, and schedule the task.

## Dependencies

- Arduino Uno board
- Sensors: Temperature sensor, humidity sensor, LDR sensor, IR sensor
- Actuators: Fan, lamp
- ThingSpeak platform
- Android SDK

## Videos

### Entire Uno and App Working
[![Entire Uno and App Working](https://i9.ytimg.com/vi_webp/XNqMgMuKFB0/mqdefault.webp?v=65ccc644&sqp=CPzdo7AG&rs=AOn4CLCS8f7x0gswfxCNbSdaAR8u2wpM2g)](https://youtu.be/XNqMgMuKFB0)

### App Screen Capture
[![App Screen Capture](https://i9.ytimg.com/vi_webp/Ilp3xv7_e3w/mqdefault.webp?v=65ccc70f&sqp=CPzdo7AG&rs=AOn4CLCKNg-N0RyMOUjLwsKYhtN_6tMNkw)](https://youtu.be/Ilp3xv7_e3w)
