package kr.ac.yuhan.cs.androidproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText uidEditText;
    private EditText upwEditText;
    private Button loginButton;
    private TextView registerText, findUserText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // 너의 login.xml 파일

        uidEditText = findViewById(R.id.uid);
        upwEditText = findViewById(R.id.upw);
        loginButton = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register);
        findUserText = findViewById(R.id.find_user);

        // 로그인 처리
        loginButton.setOnClickListener(v -> {
            String username = uidEditText.getText().toString();
            String password = upwEditText.getText().toString();

            if (username.equals("admin") && password.equals("1234")) {
                Toast.makeText(this, "테스트 계정 로그인 성공", Toast.LENGTH_SHORT).show();
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().putString("username", "admin").apply();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("token", "test-token");
                startActivity(intent);
                finish();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(username, password);
            RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().putString("username", response.body().getUsername()).apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", response.body().getToken());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인 실패 (코드: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("RetrofitError", "로그인 실패: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 회원가입 다이얼로그
        registerText.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.register);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            Button registerButton = dialog.findViewById(R.id.register_btn);
            Button cancelButton = dialog.findViewById(R.id.cancel_btn);

            EditText nameEditText = dialog.findViewById(R.id.label2);
            EditText idEditText = dialog.findViewById(R.id.label4);
            EditText pwEditText = dialog.findViewById(R.id.label6);
            EditText emailEditText = dialog.findViewById(R.id.label10);

            registerButton.setOnClickListener(v1 -> {
                String username = idEditText.getText().toString();
                String password = pwEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String displayName = nameEditText.getText().toString();

                RegisterRequest request = new RegisterRequest(username, password, email, displayName);

                RetrofitClient.getApiService().registerUser(request).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("RetrofitError", "회원가입 실패: " + t.getMessage());
                        Toast.makeText(LoginActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            cancelButton.setOnClickListener(v2 -> dialog.dismiss());
        });

        // 아이디/비밀번호 찾기 다이얼로그
        findUserText.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.find_user);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            EditText nameField = dialog.findViewById(R.id.findName);
            EditText emailField = dialog.findViewById(R.id.findEmail);
            Button submitBtn = dialog.findViewById(R.id.find_submit_btn);
            Button cancelBtn = dialog.findViewById(R.id.find_cancel_btn);

            submitBtn.setOnClickListener(v1 -> {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();

                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "입력한 정보로 계정을 찾습니다.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            cancelBtn.setOnClickListener(v2 -> dialog.dismiss());
        });
    }
}


