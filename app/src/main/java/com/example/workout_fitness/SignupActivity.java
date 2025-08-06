package com.example.workout_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "Signup Activity";

    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText weight;
    private EditText height;
    private EditText age;
    private EditText username;
    private EditText password;

    private Button submit;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        // Khởi tạo view
        back = findViewById(R.id.back);
        firstName = findViewById(R.id.etfirstname);
        lastName = findViewById(R.id.etlastname);
        emailAddress = findViewById(R.id.etEmailAddress);
        weight = findViewById(R.id.etWeight);
        height = findViewById(R.id.etHeight);
        age = findViewById(R.id.etAge);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        submit = findViewById(R.id.btnSubmit);

        // Xử lý nút submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu người dùng nhập
                String firstNameStr = firstName.getText().toString().trim();
                String lastNameStr = lastName.getText().toString().trim();
                String emailStr = emailAddress.getText().toString().trim();
                String heightStr = height.getText().toString().trim();
                String weightStr = weight.getText().toString().trim();
                String ageStr = age.getText().toString().trim();
                String usernameStr = username.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                // Kiểm tra dữ liệu rỗng
                if (firstNameStr.isEmpty()) {
                    firstName.setError("Vui lòng nhập họ");
                    return;
                }
                if (lastNameStr.isEmpty()) {
                    lastName.setError("Vui lòng nhập tên");
                    return;
                }
                if (emailStr.isEmpty()) {
                    emailAddress.setError("Vui lòng nhập email");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    emailAddress.setError("Email không hợp lệ");
                    return;
                }
                if (heightStr.isEmpty()) {
                    height.setError("Vui lòng nhập chiều cao");
                    return;
                }
                if (!heightStr.matches("\\d+")) {
                    height.setError("Chiều cao phải là số");
                    return;
                }

                if (weightStr.isEmpty()) {
                    weight.setError("Vui lòng nhập cân nặng");
                    return;
                }
                if (!weightStr.matches("\\d+")) {
                    weight.setError("Cân nặng phải là số");
                    return;
                }

                if (ageStr.isEmpty()) {
                    age.setError("Vui lòng nhập tuổi");
                    return;
                }
                if (!ageStr.matches("\\d+")) {
                    age.setError("Tuổi phải là số");
                    return;
                }
                if (usernameStr.isEmpty()) {
                    username.setError("Vui lòng nhập tên người dùng");
                    return;
                }
                if (passwordStr.isEmpty()) {
                    password.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                // Kiểm tra email đã tồn tại chưa
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", emailStr);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> users, ParseException e) {
                        if (users != null && !users.isEmpty()) {
                            // Email đã tồn tại
                            Toast.makeText(SignupActivity.this, "Email đã tồn tại trong hệ thống", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Nếu email chưa tồn tại, tiếp tục đăng ký
                        ParseUser user = new ParseUser();
                        user.setPassword(passwordStr);
                        user.setUsername(usernameStr);
                        user.setEmail(emailStr);
                        user.put("firstname", firstNameStr);
                        user.put("lastname", lastNameStr);
                        user.put("height", Integer.parseInt(heightStr));
                        user.put("weight", Integer.parseInt(weightStr));
                        user.put("age", Integer.parseInt(ageStr));

                        Toast.makeText(SignupActivity.this, "Đang đăng ký...", Toast.LENGTH_SHORT).show();

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Đăng ký người dùng thất bại", e);
                                    Toast.makeText(SignupActivity.this, "Đăng ký thất bại do email đã có người sử dụng ", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.i(TAG, "Đăng ký thành công");
                                    Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                    goMainActivity();
                                }
                            }
                        });
                    }
                });
            }
        });

        // Xử lý nút quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignupActivity.this, "Quay lại", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                Animatoo.animateSlideRight(SignupActivity.this);
                finish();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
