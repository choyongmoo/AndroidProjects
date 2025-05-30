package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FindUsernameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_username);
        // 여기에 이메일 입력 받고 버튼 누르면 Retrofit으로 서버 호출
    }
}
