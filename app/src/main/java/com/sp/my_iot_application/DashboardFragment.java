package com.sp.my_iot_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;
import com.sp.my_iot_application.DashboardPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private ViewPager2 viewPager;
    //private TextView recentDataTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        //recentDataTextView = view.findViewById(R.id.recentDataTextView); // Initialize TextView
        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        List<String> thingspeakUrls = getThingspeakUrls();

        DashboardPagerAdapter pagerAdapter = new DashboardPagerAdapter(requireActivity(), thingspeakUrls);
        viewPager.setAdapter(pagerAdapter);


    }

    private List<String> getThingspeakUrls() {
        // Provide a list of Thingspeak URLs for each page
        List<String> urls = new ArrayList<>();
        urls.add("https://thingspeak.com/channels/2348974/charts/1?bgcolor=%23ffffff&color=%23d62020&results=60&title=Temp");
        urls.add("https://thingspeak.com/channels/2348974/charts/2?bgcolor=%23ffffff&color=%23d62020&results=60&title=Humi");
        urls.add("https://thingspeak.com/channels/2348974/charts/6?bgcolor=%23ffffff&color=%23d62020&results=60&title=LDR");
        return urls;
    }


}
