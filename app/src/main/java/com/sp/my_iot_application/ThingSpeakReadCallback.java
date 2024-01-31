package com.sp.my_iot_application;

public interface ThingSpeakReadCallback {
    void onThingSpeakDataReceived(int fieldNumber, int fieldValue);
}
