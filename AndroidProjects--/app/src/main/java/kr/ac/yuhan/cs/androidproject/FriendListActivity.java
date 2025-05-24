package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// 친구 목록 화면
public class FriendListActivity extends AppCompatActivity {

    private RecyclerView friendRecyclerView;
    private FriendAdapter friendAdapter;
    private ArrayList<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

        friendRecyclerView = findViewById(R.id.friendRecyclerView);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 더미 친구 데이터
        friendList = new ArrayList<>();
        friendList.add(new Friend("김유진", "yujin@example.com"));
        friendList.add(new Friend("박동현", "donghyun@example.com"));
        friendList.add(new Friend("이서준", "seojoon@example.com"));

        friendAdapter = new FriendAdapter(friendList);
        friendRecyclerView.setAdapter(friendAdapter);
    }
}
