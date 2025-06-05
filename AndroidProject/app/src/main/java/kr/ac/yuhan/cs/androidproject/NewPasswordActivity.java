package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yuhan.cs.androidproject.dto.NewPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText editUsername, editEmail, editNewPassword;
    private Button resetButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        editNewPassword = findViewById(R.id.edit_new_password);
        resetButton = findViewById(R.id.reset_button);
        apiService = RetrofitClient.getApiService();

        resetButton.setOnClickListener(v -> {
            String username = editUsername.getText().toString();
            String email = editEmail.getText().toString();
            String newPassword = editNewPassword.getText().toString();

            NewPasswordRequest request = new NewPasswordRequest(username, email, newPassword);

            apiService.resetPassword(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "비밀번호 변경 완료", Toast.LENGTH_SHORT).show();
                        finish(); // 로그인 화면으로 돌아가기
                    } else {
                        Toast.makeText(getApplicationContext(), "실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
