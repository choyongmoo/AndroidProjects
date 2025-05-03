package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView navMenu;
    private ImageButton btnOpenDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 시스템 바 패딩 제거 (불필요하다면 삭제 가능)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 뷰 연결
        drawerLayout = findViewById(R.id.drawerLayout);
        navMenu = findViewById(R.id.navMenu);
        btnOpenDrawer = findViewById(R.id.btnOpenDrawer);

        // DrawerLayout은 처음에 메뉴가 보이지 않도록 설정해야 하므로 이 부분을 수정
        navMenu.setVisibility(View.GONE); // 처음에는 숨기기

        // 햄버거 메뉴 버튼 클릭 시 DrawerLayout 열기
        btnOpenDrawer.setOnClickListener(v -> {
            drawerLayout.openDrawer(navMenu); // 메뉴 열기
        });
    }
}
