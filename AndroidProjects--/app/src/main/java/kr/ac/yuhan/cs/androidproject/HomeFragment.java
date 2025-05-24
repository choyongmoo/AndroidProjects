package kr.ac.yuhan.cs.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

// 홈 화면 프래그먼트 (약속 생성, 약속 목록, 친구 목록 버튼 포함)
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // 기본 생성자 (필수)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // home.xml 레이아웃을 화면에 표시
        View view = inflater.inflate(R.layout.home, container, false);

        // ✅ 약속 만들기 버튼 연결 및 클릭 처리
        Button createBtn = view.findViewById(R.id.btnCreateMeeting);
        createBtn.setOnClickListener(v -> {
            // CreateMeetingActivity로 화면 전환
            Intent intent = new Intent(getActivity(), CreateMeetingActivity.class);
            startActivity(intent);
        });

        // ✅ 약속 목록 보기 버튼 연결 및 클릭 처리
        Button listBtn = view.findViewById(R.id.btnMeetingList);
        listBtn.setOnClickListener(v -> {
            // MeetingListActivity로 화면 전환
            Intent intent = new Intent(getActivity(), MeetingListActivity.class);
            startActivity(intent);
        });

        // ✅ 친구 목록 보기 버튼 연결 및 클릭 처리
        Button friendBtn = view.findViewById(R.id.btnFriendList);
        friendBtn.setOnClickListener(v -> {
            // FriendListActivity로 화면 전환
            Intent intent = new Intent(getActivity(), FriendListActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
