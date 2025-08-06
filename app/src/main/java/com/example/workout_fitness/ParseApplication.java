package com.example.workout_fitness;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Workout.class);
        ParseObject.registerSubclass(FoodLog.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("AB0vKNsGDcMwnixeBysSntIAkcBKlXQzNKBNkJGU")
                .clientKey("Alj1c5bqZaAQq4P25EKSZAoDyGYrGPXmRudMbDki")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
