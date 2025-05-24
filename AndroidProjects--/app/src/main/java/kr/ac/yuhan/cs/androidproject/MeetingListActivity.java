package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// 약속 목록을 보여주는 액티비티
public class MeetingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MeetingAdapter adapter;
    private ArrayList<Meeting> meetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_list); // 레이아웃 연결

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 더미(가짜) 약속 데이터 생성
        meetingList = new ArrayList<>();
        meetingList.add(new Meeting("스터디 모임", "2024-06-01 18:00", "홍대입구역"));
        meetingList.add(new Meeting("저녁 회식", "2024-06-03 19:30", "강남역 5번출구"));
        meetingList.add(new Meeting("프로젝트 회의", "2024-06-04 15:00", "카카오 판교"));

        // 어댑터 연결
        adapter = new MeetingAdapter(meetingList);
        recyclerView.setAdapter(adapter);
    }
}
