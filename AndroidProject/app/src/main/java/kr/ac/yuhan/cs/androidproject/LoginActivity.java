package kr.ac.yuhan.cs.androidproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            String id = uidEditText.getText().toString();
            String pw = upwEditText.getText().toString();

            // 간단한 예시: 아이디/비번이 "admin" / "1234"일 때만 로그인 성공
            if (id.equals("admin") && pw.equals("1234")) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

                // MainActivity로 이동
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 로그인 액티비티 종료
            } else {
                Toast.makeText(this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 텍스트 클릭 시 처리
        registerText.setOnClickListener(v -> {
            // 팝업창을 띄우기 위한 Dialog 생성
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.register);
            dialog.setCancelable(true);

            // 다이얼로그 크기 설정 (옵션)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // 다이얼로그 보여주기
            dialog.show();

            // 다이얼로그 내부에서 버튼 클릭 시 처리 (이 부분 DB와 연동)
            Button registerButton = dialog.findViewById(R.id.register_btn); // register.xml에 있는 버튼 ID
            registerButton.setOnClickListener(v1 -> {
                Toast.makeText(this, "회원가입 처리", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 다이얼로그 닫기
            });
            //취소시 다이얼로그 닫기
            Button cancelButton = dialog.findViewById(R.id.cancel_btn);
            cancelButton.setOnClickListener(v2 -> {
                dialog.dismiss();
            });
        });
    }
}
