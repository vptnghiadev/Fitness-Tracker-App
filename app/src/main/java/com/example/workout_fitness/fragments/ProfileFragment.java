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
    //EditText tvUsername;         // Full name: first + last
    EditText tvEmail;
    EditText tvPassword1;
    EditText tvPassword2;
    EditText tvUserHeight;
    EditText tvUserWeight;

    EditText tvUserAge;
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
        //tvUsername = view.findViewById(R.id.etUsername);  // Full name
        tvEmail = view.findViewById(R.id.etEmail);
        tvPassword1 = view.findViewById(R.id.etPassword1);
        tvPassword2 = view.findViewById(R.id.etPassword2);
        tvUserHeight = view.findViewById(R.id.etHeight);
        tvUserWeight = view.findViewById(R.id.etWeight);
        tvUserAge = view.findViewById(R.id.etAge);
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
            String email = tvEmail.getText().toString().trim();
            String height = tvUserHeight.getText().toString().trim();
            String weight = tvUserWeight.getText().toString().trim();
            String age = tvUserAge.getText().toString().trim();  // Đảm bảo bạn dùng đúng ID
            String password1 = tvPassword1.getText().toString().trim();
            String password2 = tvPassword2.getText().toString().trim();

            // 1. Kiểm tra các trường không được để trống
            if (username.isEmpty()) {
                tvName.setError("Tên không được để trống");
                tvName.requestFocus();
                return;
            }

            else if (email.isEmpty()) {
                tvEmail.setError("Email không được để trống");
                tvEmail.requestFocus();
                return;
            }

            else if (height.isEmpty()) {
                tvUserHeight.setError("Chiều cao không được để trống");
                tvUserHeight.requestFocus();
                return;
            }

            else if (weight.isEmpty()) {
                tvUserWeight.setError("Cân nặng không được để trống");
                tvUserWeight.requestFocus();
                return;
            }

            else if (age.isEmpty()) {
                tvUserAge.setError("Tuổi không được để trống");
                tvUserAge.requestFocus();
                return;
            }

            // 2. Kiểm tra định dạng email
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmail.setError("Email không hợp lệ");
                tvEmail.requestFocus();
                return;
            }

            // 3. Kiểm tra mật khẩu khớp nhau
            else if (!password1.equals(password2)) {
                tvPassword2.setError("Mật khẩu không khớp");
                tvPassword2.requestFocus();
                return;
            }

            // 4. Kiểm tra age là số > 0
            int ageVal;
            try {
                ageVal = Integer.parseInt(age);
                if (ageVal <= 0) {
                    tvUserAge.setError("Tuổi phải lớn hơn 0");
                    tvUserAge.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                tvUserAge.setError("Tuổi phải là số");
                tvUserAge.requestFocus();
                return;
            }

            // 5. Kiểm tra height và weight là số
            double heightVal, weightVal;

            // Kiểm tra chiều cao
            try {
                heightVal = Double.parseDouble(height);
                if (heightVal <= 0) {
                    tvUserHeight.setError("Chiều cao phải lớn hơn 0");
                    tvUserHeight.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                tvUserHeight.setError("Chiều cao phải là số");
                tvUserHeight.requestFocus();
                return;
            }

            // Kiểm tra cân nặng
            try {
                weightVal = Double.parseDouble(weight);
                if (weightVal <= 0) {
                    tvUserWeight.setError("Cân nặng phải lớn hơn 0");
                    tvUserWeight.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                tvUserWeight.setError("Cân nặng phải là số");
                tvUserWeight.requestFocus();
                return;
            }


            // 6. Cập nhật ParseUser
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser == null) return;

            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.put("height", heightVal);
            currentUser.put("weight", weightVal);
            currentUser.put("age", ageVal); // Ghi tuổi

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

        //String firstName = currentUser.getString("firstname");
        //String lastName = currentUser.getString("lastname");
        //tvUsername.setText(
        //        (firstName != null ? firstName : "") +
        //                (lastName != null ? " " + lastName : "")
        //);

        String email = currentUser.getEmail();
        tvEmail.setText(email != null ? email : "");

        Object heightObj = currentUser.get("height");
        Object weightObj = currentUser.get("weight");
        Object ageObj = currentUser.get("age");
        tvUserHeight.setText(heightObj != null ? heightObj.toString() : "");
        tvUserWeight.setText(weightObj != null ? weightObj.toString() : "");
        tvUserAge.setText(ageObj != null ? ageObj.toString() : "");

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
