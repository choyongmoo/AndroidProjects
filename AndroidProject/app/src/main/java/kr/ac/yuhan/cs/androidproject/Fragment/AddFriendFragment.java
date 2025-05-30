package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

import kr.ac.yuhan.cs.androidproject.dto.FriendRequestDto;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendFragment extends Fragment {

    public AddFriendFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backBtn = view.findViewById(R.id.backbtn);
        backBtn.setOnClickListener(v -> {
            Fragment homeFragment = new HomeFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, homeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button addFriendBtn = view.findViewById(R.id.btnAddFriend);
        EditText etFriendName = view.findViewById(R.id.etFriendName);

        addFriendBtn.setOnClickListener(v -> {
            String friendUsername = etFriendName.getText().toString().trim();

            if(friendUsername.isEmpty()){
                Toast.makeText(getContext(), "친구 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // DTO 생성 (이름은 상황에 맞게 변경 가능)
            FriendRequestDto request = new FriendRequestDto(friendUsername);

            RetrofitClient.getApiService().sendFriendRequest(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getContext(), "친구 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("FriendRequest", "요청 실패 코드: " + response.code());
                        Log.e("FriendRequest", "요청 실패 메시지: " + response.message());

                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("FriendRequest", "에러 바디: " + errorBody);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), "친구 요청 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            // 상단 타이틀 바 등 UI 갱신용
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("친구 추가");
            }
        }
    }
}
