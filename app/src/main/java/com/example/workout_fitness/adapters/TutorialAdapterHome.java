package com.example.workout_fitness.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.workout_fitness.models_tutorials.VideoYT;

import java.util.List;

import com.example.workout_fitness.R;

public class TutorialAdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VideoYT> videoList;

    public TutorialAdapterHome(Context context, List<VideoYT> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    public class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView imageView, ivPosterOverlay;
        TextView tvTitle, tvDescription;
        RelativeLayout rvContainer;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            rvContainer = itemView.findViewById(R.id.tutorialContainer);
            ivPosterOverlay = itemView.findViewById(R.id.ivPosterOverlay);
        }

        public void setData(VideoYT videoYT) {
            String getTitle = videoYT.getSnippet().getTitle();
            String getDescription = videoYT.getSnippet().getDescription();
            String getThumb = videoYT.getSnippet().getThumbnails().getMedium().getUrl();

            tvTitle.setText(getTitle);
            tvDescription.setText(getDescription);
            int radius = 20; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(itemView.getContext()).load(getThumb).transform(new RoundedCorners(radius)).placeholder(R.drawable.loading).into(imageView);
            Glide.with(itemView.getContext()).load(R.drawable.play_button).into(ivPosterOverlay);

            rvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String videoId = videoYT.getId().getVideoId();
                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                    intent.setPackage("com.google.android.youtube"); // Chỉ định mở bằng YouTube app

                    // Kiểm tra nếu thiết bị có cài YouTube app
                    if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                        view.getContext().startActivity(intent);
                    } else {
                        // Nếu không có, fallback mở bằng trình duyệt
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                                android.net.Uri.parse("https://www.youtube.com/watch?v=" + videoId)));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tutorial, parent, false);
        return new YoutubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoYT videoYT = videoList.get(position);
        YoutubeHolder yth = (YoutubeHolder) holder;
        yth.setData(videoYT);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
