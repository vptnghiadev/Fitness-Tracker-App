package com.example.workout_fitness.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.workout_fitness.DetectorActivity;
import com.example.workout_fitness.FoodLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.workout_fitness.R;

public class FoodFragment extends Fragment {
    private HashMap<String, Integer> calorieInfo = new HashMap<>();
    private static final int REQUEST_CODE_DETECTOR = 100;

    private View rootView;
    // Launcher để nhận kết quả từ DetectorActivity

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camera_ai, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            calorieInfo = loadCalorieInfo("calorie_info.txt", view.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetectorActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DETECTOR);
        });
        // Load lịch sử từ Back4App
        ParseQuery<FoodLog> query = ParseQuery.getQuery(FoodLog.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser()); // ⬅️ Chỉ lấy log của user hiện tại
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<FoodLog>() {
            @Override
            public void done(List<FoodLog> logs, ParseException e) {
                if (e == null) {
                    for (FoodLog log : logs) {
                        addFoodToTable(log.getFoodName(), log.getCalories(), log.getTimestamp(), view);
                    }
                } else {
                    Log.e("ParseQuery", "Error loading user logs", e);
                }
            }
        });
    }

    private HashMap<String, Integer> loadCalorieInfo(String filename, Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
        HashMap<String, Integer> calCounts = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                calCounts.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        return calCounts;
    }

    public void addFood(String food, View view) {
        int calories = getCalorie(food);
        String timeString = new SimpleDateFormat("h:mm a", Locale.getDefault())
                .format(new Date(System.currentTimeMillis()));
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Lưu vào Parse
        if (currentUser != null) {
            FoodLog log = new FoodLog();
            log.setFoodName(food);
            log.setCalories(calories);
            log.setTimestamp(timeString);
            log.setUser(currentUser); // ⬅️ Gắn user hiện tại

            log.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("Back4App", "Saved with user: " + currentUser.getUsername());
                    } else {
                        Log.e("Back4App", "Save failed", e);
                    }
                }
            });
        }

        // Hiển thị trên bảng
        addFoodToTable(food, calories, timeString, view);
    }

    private void addFoodToTable(String food, int calories, String timestamp, View view) {
        TextView total = view.findViewById(R.id.total);
        TextView progressBar_total = view.findViewById(R.id.progressBar_total);
        ProgressBar progressBar = view.findViewById(R.id.progressBar3);
        TableLayout tableLayout = view.findViewById(R.id.tablelayout);

        int cur = Integer.parseInt(total.getText().toString());
        cur += calories;

        total.setText(String.valueOf(cur));
        progressBar_total.setText("Calo: " + cur);
        progressBar.setProgress(cur);

        TableRow row = new TableRow(view.getContext());
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        TextView timeText = createCenteredTextView(view, timestamp);
        TextView foodText = createCenteredTextView(view, food);
        TextView calText = createCenteredTextView(view, String.valueOf(calories));

        row.addView(timeText);
        row.addView(foodText);
        row.addView(calText);
        tableLayout.addView(row);
    }

    private TextView createCenteredTextView(View view, String text) {
        TextView tv = new TextView(view.getContext());
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tv.setLayoutParams(params);
        return tv;
    }

    private int getCalorie(String food) {
        Integer cal = calorieInfo.get(food);
        return cal != null ? cal : 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("FoodFragment", "onActivityResult called");

        if (requestCode == REQUEST_CODE_DETECTOR && resultCode == Activity.RESULT_OK && data != null) {
            String food = data.getStringExtra("detected_food");
            Log.d("FoodFragment", "Detected food from intent: " + food);

            if (food != null && getView() != null) {
                addFood(food, getView());
            } else {
                Log.e("FoodFragment", "Food is null or getView() is null");
            }
        } else {
            Log.e("FoodFragment", "Invalid result or null data");
        }
    }
}
