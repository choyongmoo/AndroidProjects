package kr.ac.yuhan.cs.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerFriendList;
    private FriendAdapter adapter;

    public FriendListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_list, container, false);

        recyclerFriendList = view.findViewById(R.id.recyclerFriendList);
        recyclerFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FriendAdapter();
        recyclerFriendList.setAdapter(adapter);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button btnFriendRequests = view.findViewById(R.id.btnFriendRequests);
        btnFriendRequests.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FriendRequestFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        loadFriends(); // 친구 목록 불러오기 호출

        return view;
    }


    private void loadFriends() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getFriends().enqueue(new Callback<List<GetUserResponse>>() {
            @Override
            public void onResponse(Call<List<GetUserResponse>> call, Response<List<GetUserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setFriends(response.body());
                } else {
                    Log.e("FriendRequest", "실패: 응답 코드 = " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("FriendRequest", "에러 내용 = " + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "친구 목록 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetUserResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("친구 목록");
            }
        }
    }
}
