package kr.ac.yuhan.cs.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class InfoFragment extends Fragment {

    public InfoFragment(){
        super(R.layout.info);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // SharedPreferences에서 username 불러오기
        Context context = getActivity();
        if (context != null) {
            String username = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    .getString("username", ""); // 저장된 username 없으면 빈문자열

            TextView infoIdView = view.findViewById(R.id.infoIdView);
            infoIdView.setText(username);
        }

        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> {
            // HomeFragment로 전환
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("프로필 관리");
            }
        }
    }
}
