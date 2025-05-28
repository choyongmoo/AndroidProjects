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
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // login.xml 연결

        uidEditText = findViewById(R.id.uid);
        upwEditText = findViewById(R.id.upw);
        loginButton = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register);

        // 로그인 버튼 클릭 시
        loginButton.setOnClickListener(v -> {
            String username = uidEditText.getText().toString();
            String password = upwEditText.getText().toString();

            LoginRequest loginRequest = new LoginRequest(username, password);

            RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                        String loggedInUsername = response.body().getUsername(); // 또는 getDisplayName()

                        // SharedPreferences에 저장
                        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("username", loggedInUsername)
                                .apply();

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
                    Log.e("RetrofitError", "로그인 통신 실패: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 회원가입 텍스트 클릭 시 처리
        registerText.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.register);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            // 회원가입 버튼 클릭
            Button registerButton = dialog.findViewById(R.id.register_btn);
            registerButton.setOnClickListener(v1 -> {
                EditText nameEditText = dialog.findViewById(R.id.label2);   // 이름
                EditText idEditText = dialog.findViewById(R.id.label4);     // ID
                EditText pwEditText = dialog.findViewById(R.id.label6);     // PW
                EditText emailEditText = dialog.findViewById(R.id.label10); // 이메일

                String username = idEditText.getText().toString();
                String password = pwEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String displayName = nameEditText.getText().toString();

                RegisterRequest request = new RegisterRequest(username, password, email, displayName);

                RetrofitClient.getApiService().registerUser(request).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(LoginActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "회원가입 실패 (코드: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("RetrofitError", "회원가입 통신 실패: " + t.getMessage());
                        Toast.makeText(LoginActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });

            // 취소 버튼 처리
            Button cancelButton = dialog.findViewById(R.id.cancel_btn);
            cancelButton.setOnClickListener(v2 -> dialog.dismiss());
        });
    }
}
