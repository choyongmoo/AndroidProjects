package kr.ac.yuhan.cs.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import kr.ac.yuhan.cs.androidproject.Fragment.AddFriendFragment;
import kr.ac.yuhan.cs.androidproject.Fragment.FriendListFragment;
import kr.ac.yuhan.cs.androidproject.Fragment.HomeFragment;
import kr.ac.yuhan.cs.androidproject.Fragment.InfoFragment;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        RetrofitClient.initTokenManager(getApplicationContext());

        drawerLayout = findViewById(R.id.drawerLayout);
        ImageButton btnOpenDrawer = findViewById(R.id.btnOpenDrawer);

        btnOpenDrawer.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

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

        // 1. SharedPreferences에서 username만 가져오기
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username == null) {
            // 로그인 안 된 상태면 로그인 화면으로 이동
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 2. 서버에서 사용자 정보 불러오기 (비동기)
        loadUserInfoFromServer(username);

        // 네비게이션 메뉴 처리
        TextView navHome = findViewById(R.id.nav_home);
        TextView navProfile = findViewById(R.id.nav_profile);
        TextView navAddFriend = findViewById(R.id.nav_add_friend);
        TextView navFriendList = findViewById(R.id.nav_friend_list);
        TextView navLogout = findViewById(R.id.nav_logout);

        navHome.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navProfile.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new InfoFragment())
                    .commit();
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FriendListFragment())
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserInfoFromServer(String username) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getUserInfo(username).enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetUserResponse user = response.body();

                    TextView userNameTop = findViewById(R.id.userName);
                    TextView userNameNav = findViewById(R.id.userName1);
                    ImageView userImageTop = findViewById(R.id.userProfileImage);
                    ImageView userImageNav = findViewById(R.id.userProfileImage1);

                    userNameTop.setText(user.getDisplayName());
                    userNameNav.setText(user.getDisplayName());

                    String imageUrl = user.getProfileImageUrl();

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(MainActivity.this)
                                .load(imageUrl)
                                .signature(new ObjectKey(System.currentTimeMillis()))
                                .placeholder(R.drawable.default_profile)
                                .error(R.drawable.default_profile)
                                .circleCrop()
                                .into(userImageTop);

                        Glide.with(MainActivity.this)
                                .load(imageUrl)
                                .signature(new ObjectKey(System.currentTimeMillis()))
                                .placeholder(R.drawable.default_profile)
                                .error(R.drawable.default_profile)
                                .circleCrop()
                                .into(userImageNav);
                    } else {
                        Glide.with(MainActivity.this)
                                .load(R.drawable.default_profile)
                                .circleCrop()
                                .into(userImageTop);

                        Glide.with(MainActivity.this)
                                .load(R.drawable.default_profile)
                                .circleCrop()
                                .into(userImageNav);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버와 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username != null) {
            loadUserInfoFromServer(username);
        }
    }

    public void refreshUserInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username != null) {
            loadUserInfoFromServer(username);
        }
    }

}
