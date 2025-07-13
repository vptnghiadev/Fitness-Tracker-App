package edu.csueb.codepath.fitness_tracker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import edu.csueb.codepath.fitness_tracker.fragments.FoodFragment;

public class FoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera_ai); // layout sẽ chứa fragment

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FoodFragment())
                    .commit();
        }
    }
}
