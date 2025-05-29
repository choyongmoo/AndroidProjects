package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class CreateMeetingFragment extends Fragment {

    public CreateMeetingFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_meeting, container, false);

        // "뒤로가기" 버튼 클릭 시 HomeFragment로 이동
        MaterialButton cancelButton = view.findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(v -> navigateToHomeFragment());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 툴바 제목 설정
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("약속 만들기");
            }
        }
    }

    private void navigateToHomeFragment() {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.addToBackStack(null);  // Optional: 뒤로가기로 복귀 가능하게
        transaction.commit();
    }
}
