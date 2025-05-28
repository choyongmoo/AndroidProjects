package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        MaterialButton btnCreateMeeting = rootView.findViewById(R.id.btnCreateMeeting);
        btnCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CreateMeetingFragment 인스턴스 생성
                CreateMeetingFragment createMeetingFragment = new CreateMeetingFragment();

                // FragmentTransaction 시작
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // 현재 프래그먼트를 CreateMeetingFragment로 교체
                transaction.replace(R.id.fragment_container, createMeetingFragment);

                // 뒤로가기 시 이전 프래그먼트로 돌아갈 수 있도록 백스택에 추가
                transaction.addToBackStack(null);

                // 변경사항 커밋
                transaction.commit();
            }
        });

        return rootView;
    }

    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("홈 화면");
            }
        }
    }
}
