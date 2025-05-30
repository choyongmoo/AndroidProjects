package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import kr.ac.yuhan.cs.androidproject.R;

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
                CreateMeetingFragment createMeetingFragment = new CreateMeetingFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, createMeetingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        MaterialButton btnGroupManage = rootView.findViewById(R.id.btnGroupManage);
        btnGroupManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupFragment groupFragment = new GroupFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, groupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        MaterialButton btnMeetingList = rootView.findViewById(R.id.btnMeetingList);
        btnMeetingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingFragment meetingFragment = new MeetingFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, meetingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    @Override
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
