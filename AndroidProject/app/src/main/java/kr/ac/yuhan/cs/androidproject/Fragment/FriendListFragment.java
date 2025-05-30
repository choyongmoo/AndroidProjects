package kr.ac.yuhan.cs.androidproject.Fragment;

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

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.Adapter.FriendAdapter;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
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
        loadGroups();  // 그룹 목록 불러오기 호출 추가

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

    private void loadGroups() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getGroups().enqueue(new Callback<List<GroupSummary>>() {
            @Override
            public void onResponse(Call<List<GroupSummary>> call, Response<List<GroupSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setGroups(response.body());
                } else {
                    Log.e("FriendListFragment", "그룹 목록 불러오기 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GroupSummary>> call, Throwable t) {
                Log.e("FriendListFragment", "그룹 목록 서버 연결 실패: " + t.getMessage());
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
