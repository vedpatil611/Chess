package com.mad_project.chess.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad_project.chess.R;

public class Auth extends FragmentActivity implements LoginFragment.LoginInterface, SignupFragment.SignupInterface {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();

        tabLayout = findViewById(R.id.auth_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.auth_view_pager);
        viewPager.setAdapter(new AuthTabAdapter(Auth.this, tabLayout.getTabCount()));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(Auth.this, HomeScreen.class));
            finish();
        }
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(Auth.this, HomeScreen.class));
                        Auth.this.finish();
                    } else {
                        Toast.makeText(Auth.this, "Incorrect email id or password", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void signup(String email, String password, String cpassword) {
        if(!password.equals(cpassword))
            Toast.makeText(this, "Password does not match", Toast.LENGTH_LONG).show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        mAuth.signOut();
                        viewPager.setCurrentItem(0);
                        Toast.makeText(Auth.this, "Signed in successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Auth.this, "Failed to sign up", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static class AuthTabAdapter extends FragmentStateAdapter {

        int count;

        public AuthTabAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
            super(fragmentActivity);
            this.count = count;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                default:
                    return new LoginFragment();
                case 1:
                    return new SignupFragment();
            }
        }

        @Override
        public int getItemCount() {
            return count;
        }
    }
}
