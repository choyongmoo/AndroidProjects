package kr.ac.yuhan.cs.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.LoginRequest;
import kr.ac.yuhan.cs.androidproject.dto.LoginResponse;
import kr.ac.yuhan.cs.androidproject.dto.RegisterRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText uidEditText;
    private EditText upwEditText;
    private Button loginButton;
    private TextView registerText;
    private TextView findUserText;  // 추가: 아이디/비밀번호 찾기 텍스트

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        RetrofitClient.initTokenManager(this);
        tokenManager = new TokenManager(this);

        uidEditText = findViewById(R.id.uid);
        upwEditText = findViewById(R.id.upw);
        loginButton = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register);

        // find_user TextView 추가 바인딩 (layout XML에 find_user 아이디 있어야 함)
        findUserText = findViewById(R.id.find_user);

        loginButton.setOnClickListener(v -> {
            String username = uidEditText.getText().toString();
            String password = upwEditText.getText().toString();

            LoginRequest loginRequest = new LoginRequest(username, password);

            RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();
                        String loggedInUsername = response.body().getUsername();

                        // 토큰 저장
                        tokenManager.saveToken(token);

                        // Retrofit 재설정 (토큰 반영 위해)
                        RetrofitClient.initTokenManager(LoginActivity.this);

                        // 사용자 정보 요청
                        RetrofitClient.getApiService().getUserInfo(loggedInUsername)
                                .enqueue(new Callback<GetUserResponse>() {
                                    @Override
                                    public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> userResponse) {
                                        if (userResponse.isSuccessful() && userResponse.body() != null) {
                                            GetUserResponse userInfo = userResponse.body();

                                            // SharedPreferences 저장
                                            SharedPreferences.Editor editor = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit();
                                            editor.putString("username", userInfo.getUsername());
                                            editor.putString("displayName", userInfo.getDisplayName());
                                            editor.putString("email", userInfo.getEmail());
                                            editor.apply();

                                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("token", token);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "사용자 정보 불러오기 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GetUserResponse> call, Throwable t) {
                                        Toast.makeText(LoginActivity.this, "사용자 정보 요청 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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

        registerText.setOnClickListener(v -> showRegisterDialog());

        // find_user 클릭시 아이디/비밀번호 찾기 다이얼로그 띄우기
        findUserText.setOnClickListener(v -> showFindDialog());
    }

    private void showRegisterDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.register);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button registerButton = dialog.findViewById(R.id.register_btn);
        Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        registerButton.setOnClickListener(v -> {
            EditText nameEditText = dialog.findViewById(R.id.label2);
            EditText idEditText = dialog.findViewById(R.id.label4);
            EditText pwEditText = dialog.findViewById(R.id.label6);
            EditText emailEditText = dialog.findViewById(R.id.label10);

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

        cancelButton.setOnClickListener(v -> dialog.dismiss());
    }

    // 새로 추가: 아이디 / 비밀번호 찾기 선택 다이얼로그
    private void showFindDialog() {
        final String[] options = {"아이디 찾기", "비밀번호 찾기"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("아이디 / 비밀번호 찾기 선택")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) { // 아이디 찾기 선택
                            Intent intent = new Intent(LoginActivity.this, FindUsernameActivity.class);
                            startActivity(intent);
                        } else if (which == 1) { // 비밀번호 찾기 선택
                            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
}
