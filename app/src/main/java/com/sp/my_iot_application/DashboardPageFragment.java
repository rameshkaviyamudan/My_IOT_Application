package com.sp.my_iot_application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.webkit.WebViewClient;
import android.widget.TextView;

// ... (Existing code)

public class DashboardPageFragment extends Fragment {

    // ... (Existing code)

    private static final String ARG_URL = "arg_url";
    private static final String ARG_FIELD_NUMBER = "arg_field_number";
    private WebView webView;
    private TextView lastValueTextView;

    public static DashboardPageFragment newInstance(String url, int fieldNumber) {
        DashboardPageFragment fragment = new DashboardPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putInt(ARG_FIELD_NUMBER, fieldNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_page, container, false);

        webView = view.findViewById(R.id.webView);
        lastValueTextView = view.findViewById(R.id.lastValueTextView); // Initialize TextView

        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString(ARG_URL);
            int fieldNumber = args.getInt(ARG_FIELD_NUMBER);
            loadWebView(url, fieldNumber);
        }

        return view;
    }
    private void loadWebView(String url, int fieldNumber) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Set the initial scale once the page is loaded
                webView.setInitialScale(200);
            }
        });

        // Disable both horizontal and vertical scroll bars
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        webView.loadUrl(url);

        // Use SharedPreferences to get the last value
        String fieldName;
        float lastValue = getLastSensorValue(fieldNumber);

        switch (fieldNumber) {
            case 1:
                fieldName = "Temperature";
                break;
            case 2:
                fieldName = "Humidity";
                break;
            case 3:
                fieldName = "Environment Lighting";
                break;
            default:
                fieldName = "Field " + fieldNumber;
                break;
        }

        lastValueTextView.setText("Your most recent " + fieldName + " is: " + lastValue);
    }

    // Method to retrieve the last value from SharedPreferences
    private float getLastSensorValue(int fieldNumber) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("SensorData", Context.MODE_PRIVATE);

        // Adjust the field number if it is 6
        int adjustedFieldNumber = (fieldNumber == 3) ? 6 : fieldNumber;

        switch (adjustedFieldNumber) {
            case 1:
                return preferences.getInt("field_1", 0);
            case 2:
                return preferences.getInt("field_2", 0);
            case 6:
                return preferences.getInt("field_6", 0);
            default:
                return 0;
        }
    }


}
