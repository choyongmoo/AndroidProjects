package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.NewPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editUsername, editEmail, editNewPassword, editConfirmPassword;
    private Button btnVerifyUser, btnChangePassword;

    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        editUsername = findViewById(R.id.usernameEditText);
        editEmail = findViewById(R.id.emailEditText);
        editNewPassword = findViewById(R.id.newPasswordEditText);
        editConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        btnVerifyUser = findViewById(R.id.verifyUserButton);
        btnChangePassword = findViewById(R.id.resetPasswordButton);

        setPasswordFieldsEnabled(false);

        btnVerifyUser.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            Log.d("ResetPassword", "VerifyUser clicked - username: " + username + ", email: " + email);

            if (username.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "아이디와 이메일을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitClient.getApiService().getUserInfo(username)
                    .enqueue(new Callback<GetUserResponse>() {
                        @Override
                        public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                            Log.d("ResetPassword", "VerifyUser response code: " + response.code());
                            if (response.isSuccessful() && response.body() != null) {
                                GetUserResponse userInfo = response.body();
                                Log.d("ResetPassword", "User info from server: email=" + userInfo.getEmail());

                                if (userInfo.getEmail() != null && userInfo.getEmail().equalsIgnoreCase(email)) {
                                    Toast.makeText(getApplicationContext(), "사용자 확인 완료. 비밀번호를 변경할 수 있습니다.", Toast.LENGTH_SHORT).show();
                                    currentUsername = username;
                                    setPasswordFieldsEnabled(true);
                                } else {
                                    Toast.makeText(getApplicationContext(), "이메일이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    setPasswordFieldsEnabled(false);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                setPasswordFieldsEnabled(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUserResponse> call, Throwable t) {
                            Log.e("ResetPassword", "VerifyUser onFailure", t);
                            Toast.makeText(getApplicationContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            setPasswordFieldsEnabled(false);
                        }
                    });
        });


        btnChangePassword.setOnClickListener(v -> {
            String newPassword = editNewPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();

            if (currentUsername == null) {
                Toast.makeText(this, "먼저 아이디 및 이메일 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            NewPasswordRequest request = new NewPasswordRequest(
                    currentUsername,
                    editEmail.getText().toString(),
                    newPassword
            );

            RetrofitClient.getApiService().changePassword(request)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                setPasswordFieldsEnabled(false);
                            } else {
                                Toast.makeText(getApplicationContext(), "비밀번호 변경 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void setPasswordFieldsEnabled(boolean enabled) {
        editNewPassword.setEnabled(enabled);
        editConfirmPassword.setEnabled(enabled);
        btnChangePassword.setEnabled(enabled);
    }
}
