package com.example.educonnect;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TutorDashboardPagerAdapter extends FragmentStateAdapter {

    public TutorDashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the appropriate fragment for each tab
        if (position == 0) {
            return new PostsFragment();
        } else {
            return new ChatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}
