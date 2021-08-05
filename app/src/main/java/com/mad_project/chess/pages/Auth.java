package com.mad_project.chess.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mad_project.chess.R;

public class Auth extends FragmentActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tabLayout = findViewById(R.id.auth_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.auth_view_pager);
        viewPager.setAdapter(new AuthTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(this);
    }

    // Tab on selected listners
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static class AuthTabAdapter extends FragmentStatePagerAdapter {

        private final int count;

        public AuthTabAdapter(@NonNull FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                default:
                    return new LoginFragment();
                case 2:
                    return new SignupFragment();
            }
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
