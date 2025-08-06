package com.example.workout_fitness;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.workout_fitness.fragments.FoodFragment;
import com.example.workout_fitness.fragments.HomeFragment;
import com.example.workout_fitness.fragments.LearnFragment;
import com.example.workout_fitness.fragments.ProfileFragment;
import com.example.workout_fitness.fragments.TrackFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.workout_fitness.R;

public class MainActivity extends AppCompatActivity {

    final FragmentManager mFragmentManager = getSupportFragmentManager();
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNavigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_learn:
                        fragment = new LearnFragment();
                        break;
                    case R.id.action_camara:
                        fragment = new FoodFragment();
                        break;
                    case R.id.action_track:
                        fragment = new TrackFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}