package com.example.workout_fitness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.workout_fitness.LoginActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import com.example.workout_fitness.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    EditText tvName;             // Username (not full name)
    EditText tvUsername;         // Full name: first + last
    EditText tvEmail;
    EditText tvPassword1;
    EditText tvPassword2;
    EditText tvUserHeight;
    EditText tvUserWeight;
    ImageView ivProfileImage;
    ImageButton btnLogout;
    Button btnSave;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.etName);          // Username
        tvUsername = view.findViewById(R.id.etUsername);  // Full name
        tvEmail = view.findViewById(R.id.etEmail);
        tvPassword1 = view.findViewById(R.id.etPassword1);
        tvPassword2 = view.findViewById(R.id.etPassword2);
        tvUserHeight = view.findViewById(R.id.etHeight);
        tvUserWeight = view.findViewById(R.id.etWeight);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSave = view.findViewById(R.id.btnSave);

        getCurrentUser();

        btnLogout.setOnClickListener(v -> {
            ParseUser.logOut();
            Intent i = new Intent(getContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        btnSave.setOnClickListener(v -> {
            String username = tvName.getText().toString().trim();
            String fullName = tvUsername.getText().toString().trim();
            String email = tvEmail.getText().toString().trim();
            String height = tvUserHeight.getText().toString().trim();
            String weight = tvUserWeight.getText().toString().trim();
            String password1 = tvPassword1.getText().toString().trim();
            String password2 = tvPassword2.getText().toString().trim();

            // Kiểm tra định dạng email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmail.setError("Email không hợp lệ");
                tvEmail.requestFocus();
                return;
            }

            if (!password1.equals(password2)) {
                tvPassword2.setError("Mật khẩu không khớp");
                return;
            }

            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null) return;

            currentUser.setUsername(username);
            currentUser.setEmail(email);

            String[] nameParts = fullName.split(" ", 2);
            currentUser.put("firstname", nameParts.length > 0 ? nameParts[0] : "");
            currentUser.put("lastname", nameParts.length > 1 ? nameParts[1] : "");

            try {
                double heightVal = Double.parseDouble(height);
                double weightVal = Double.parseDouble(weight);
                currentUser.put("height", heightVal);
                currentUser.put("weight", weightVal);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Chiều cao và cân nặng phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password1.isEmpty()) {
                currentUser.setPassword(password1);
            }

            currentUser.saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(getContext(), "Đã lưu thay đổi thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error updating profile: " + e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });


    }

    private void getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "No current user found.");
            return;
        }

        Log.i(TAG, "User: @" + currentUser.getUsername());

        String username = currentUser.getUsername();
        tvName.setText(username != null ? username : "");

        String firstName = currentUser.getString("firstname");
        String lastName = currentUser.getString("lastname");
        tvUsername.setText(
                (firstName != null ? firstName : "") +
                        (lastName != null ? " " + lastName : "")
        );

        String email = currentUser.getEmail();
        tvEmail.setText(email != null ? email : "");

        Object heightObj = currentUser.get("height");
        Object weightObj = currentUser.get("weight");
        tvUserHeight.setText(heightObj != null ? heightObj.toString() : "");
        tvUserWeight.setText(weightObj != null ? weightObj.toString() : "");

        ParseFile profileImageFile = currentUser.getParseFile("profile_image");
        if (profileImageFile != null) {
            Glide.with(requireContext())
                    .load(profileImageFile.getUrl())
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.default_profile_pic)
                    .into(ivProfileImage);
        } else {
            ivProfileImage.setImageResource(R.drawable.default_profile_pic);
        }
    }
}
