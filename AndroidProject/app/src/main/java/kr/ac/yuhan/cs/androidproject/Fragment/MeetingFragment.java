package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.dto.Meeting;
import kr.ac.yuhan.cs.androidproject.Adapter.MeetingAdapter;
import kr.ac.yuhan.cs.androidproject.R;

public class MeetingFragment extends Fragment {

    public MeetingFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meeting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMeetingList = view.findViewById(R.id.rvMeetingList);

        List<Meeting> meetingList = new ArrayList<>();
        meetingList.add(new Meeting("친구들과 모임", "2025-05-29 19:00", "카페 ABC"));
        meetingList.add(new Meeting("스터디 모임", "2025-06-01 14:00", "도서관 3층"));

        MeetingAdapter adapter = new MeetingAdapter(meetingList);
        rvMeetingList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMeetingList.setAdapter(adapter);

        MaterialButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // 이전 프래그먼트로 돌아가기
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("약속 목록");
            }
        }
    }
}
