package com.ig.igdownloader.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ig.igdownloader.R;
import com.ig.igdownloader.fragments.MainActivityFragment;
import com.ig.igdownloader.fragments.PhotoActivityFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Fragment fragment1 = new MainActivityFragment();
    Fragment fragment2 = new PhotoActivityFragment();
    Fragment active = fragment1;
    FragmentManager fm = getSupportFragmentManager();
    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        if (item.getItemId() == R.id.video_downloader) {
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
            return true;
        } else if (item.getItemId() == R.id.photo_downloader) {
            fm.beginTransaction().hide(active).show(fragment2).commit();
            active = fragment2;
            return true;
        }
        return false;
    };
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);


        navigationView = findViewById(R.id.fragment_bottom_nav);
        navigationView.setSelectedItemId(R.id.video_downloader);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.primaryTextWhite)));
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        fm.beginTransaction().replace(R.id.container1, fragment1, "1").commit();
        fm.beginTransaction().add(R.id.container1, fragment2, "2").hide(fragment2).commit();
    }

}