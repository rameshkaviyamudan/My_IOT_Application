package com.sp.my_iot_application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class DashboardPagerAdapter extends FragmentStateAdapter {

    private final List<String> thingspeakUrls;

    public DashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> thingspeakUrls) {
        super(fragmentActivity);
        this.thingspeakUrls = thingspeakUrls;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DashboardPageFragment.newInstance(thingspeakUrls.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return thingspeakUrls.size();
    }
}
