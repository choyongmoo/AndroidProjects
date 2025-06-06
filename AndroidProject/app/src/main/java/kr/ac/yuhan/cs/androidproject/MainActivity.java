package kr.ac.yuhan.cs.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        ImageButton btnOpenDrawer = findViewById(R.id.btnOpenDrawer);

        btnOpenDrawer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        TextView navProfile = findViewById(R.id.nav_profile);
        TextView navAddFriend = findViewById(R.id.nav_add_friend);
        TextView navFriendList = findViewById(R.id.nav_friend_list);
        TextView navLogout = findViewById(R.id.nav_logout);

        navProfile.setOnClickListener(v -> {
            // InfoFragment로 전환
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new InfoFragment())
                    .commit();

            // 사이드 메뉴 닫기
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navAddFriend.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddFriendFragment())
                    .commit();

            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navFriendList.setOnClickListener(v -> {
            Toast.makeText(this, "친구 목록 클릭됨", Toast.LENGTH_SHORT).show();
        });

        navLogout.setOnClickListener(v -> {
            // LoginActivity로 이동하는 Intent 생성
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            // 현재 액티비티 종료 (뒤로가기 눌러도 다시 돌아오지 않게)
            finish();

            // 사이드 메뉴 닫기
            drawerLayout.closeDrawer(GravityCompat.START);

            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        });

    }
}
