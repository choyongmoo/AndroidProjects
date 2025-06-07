package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.yuhan.cs.androidproject.dto.FindIdRequest;
import kr.ac.yuhan.cs.androidproject.dto.FindIdResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindUsernameActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button findUsernameButton;
    private TextView resultText;

    private ApiService apiService; // Retrofit API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_username);

        emailEditText = findViewById(R.id.emailEditText);
        findUsernameButton = findViewById(R.id.findUsernameButton);
        resultText = findViewById(R.id.resultText);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Retrofit 클라이언트 초기화
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        findUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(FindUsernameActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                findUsername(email);
            }
        });
    }

    private void findUsername(String email) {
        FindIdRequest request = new FindIdRequest(email);

        apiService.findUsernameByEmail(request).enqueue(new Callback<FindIdResponse>() {
            @Override
            public void onResponse(Call<FindIdResponse> call, Response<FindIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String username = response.body().getUsername();
                    resultText.setText("회원님의 아이디는: " + username);
                } else {
                    resultText.setText("");
                    Toast.makeText(FindUsernameActivity.this, "아이디를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FindIdResponse> call, Throwable t) {
                resultText.setText("");
                Toast.makeText(FindUsernameActivity.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
