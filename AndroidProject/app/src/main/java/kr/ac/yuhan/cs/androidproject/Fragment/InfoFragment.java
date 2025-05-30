package kr.ac.yuhan.cs.androidproject.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.UpdateUserRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {

    public InfoFragment() {
        super(R.layout.info);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();
        if (context == null) return;

        // SharedPreferences에서 username과 token 가져오기
        String username = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("username", "");

        TextView infoIdView = view.findViewById(R.id.infoIdView);
        EditText displayNameEdit = view.findViewById(R.id.infoEditDisplayName);
        EditText passwordEdit = view.findViewById(R.id.infoEditPwd);
        EditText emailEdit = view.findViewById(R.id.infoEditEmail);

        Button modifyBtn = view.findViewById(R.id.register_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        // 1. 서버에서 사용자 정보 가져오기 (헤더 토큰은 RetrofitClient 내부에서 자동 처리)
        RetrofitClient.getApiService().getUserInfo(username)
                .enqueue(new Callback<GetUserResponse>() {
                    @Override
                    public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GetUserResponse user = response.body();
                            infoIdView.setText(user.getUsername());
                            displayNameEdit.setText(user.getDisplayName());
                            emailEdit.setText(user.getEmail());

                            // 비밀번호는 서버에서 보내지 않으므로 빈칸 유지
                            passwordEdit.setText("");
                        } else {
                            Toast.makeText(context, "사용자 정보 불러오기 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                            infoIdView.setText(username);  // 최소한 아이디는 보여줌
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserResponse> call, Throwable t) {
                        Toast.makeText(context, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        infoIdView.setText(username);  // 최소한 아이디는 보여줌
                    }
                });

        // 수정하기 버튼 클릭 리스너
        modifyBtn.setOnClickListener(v -> {
            String displayName = displayNameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String email = emailEdit.getText().toString();

            new AlertDialog.Builder(requireContext())
                    .setTitle("프로필 수정")
                    .setMessage("정말로 수정하시겠습니까?")
                    .setPositiveButton("네", (dialog, which) -> {
                        UpdateUserRequest request = new UpdateUserRequest(email, password, displayName, null);

                        RetrofitClient.getApiService().updateUser(username, request)
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("displayName", displayName);
                                            editor.apply();

                                            TextView userNameTop = requireActivity().findViewById(R.id.userName);
                                            TextView userNameNav = requireActivity().findViewById(R.id.userName1);
                                            if (userNameTop != null) userNameTop.setText(displayName);
                                            if (userNameNav != null) userNameNav.setText(displayName);

                                            Toast.makeText(getContext(), "정보가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();

                                            FragmentTransaction transaction = requireActivity()
                                                    .getSupportFragmentManager()
                                                    .beginTransaction();
                                            transaction.replace(R.id.fragment_container, new HomeFragment());
                                            transaction.commit();
                                        } else {
                                            Toast.makeText(getContext(), "수정 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(), "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("아니요", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // 취소 버튼 클릭 리스너
        cancelBtn.setOnClickListener(v -> {
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
