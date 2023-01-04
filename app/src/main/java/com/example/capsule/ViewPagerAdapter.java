package com.example.capsule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.capsule.fragments.HomeFragment;
import com.example.capsule.fragments.UploadFragment;
import com.example.capsule.fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new UploadFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new HomeFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
