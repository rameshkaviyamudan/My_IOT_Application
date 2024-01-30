package com.sp.my_iot_application;

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

        // Use a different URL for fetching the last data
        String lastDataUrl;

        switch (fieldNumber) {
            case 1:
                lastDataUrl = "https://api.thingspeak.com/channels/2348974/fields/1/last.json?api_key=HYIS4JRXOUDEEVC1";
                break;
            case 2:
                lastDataUrl = "https://api.thingspeak.com/channels/2348974/fields/2/last.json?api_key=HYIS4JRXOUDEEVC1";
                break;
            case 3:
                lastDataUrl = "https://api.thingspeak.com/channels/2348974/fields/6/last.json?api_key=HYIS4JRXOUDEEVC1";
                break;
            default:
                // Handle other cases if needed
                lastDataUrl = "";
        }

        readThingSpeakData(lastDataUrl, fieldNumber);
    }

    private void readThingSpeakData(String apiUrl, int fieldNumber) {
        int adjustedFieldNumber = (fieldNumber == 3) ? 6 : fieldNumber;

        ThingSpeakReadApiTask readApiTask = new ThingSpeakReadApiTask(requireActivity(), lastValueTextView, adjustedFieldNumber);
        readApiTask.execute(apiUrl);
    }
}
