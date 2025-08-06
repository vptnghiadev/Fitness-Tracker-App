package com.example.workout_fitness.models_tutorials;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class VideoID {

    @SerializedName("videoId")
    @Expose
    protected String videoId;

    public VideoID() { }

    public VideoID(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
