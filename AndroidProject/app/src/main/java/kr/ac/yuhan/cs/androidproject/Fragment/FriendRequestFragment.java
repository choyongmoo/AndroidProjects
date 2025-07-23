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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.Adapter.FriendRequestAdapter;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestFragment extends Fragment {

    private RecyclerView recyclerFriendRequest;
    private FriendRequestAdapter adapter;

    public FriendRequestFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);

        recyclerFriendRequest = view.findViewById(R.id.recyclerFriendRequest);
        recyclerFriendRequest.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FriendRequestAdapter(RetrofitClient.getApiService());
        recyclerFriendRequest.setAdapter(adapter);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FriendListFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadFriendRequests();

        return view;
    }

    private void loadFriendRequests() {
        RetrofitClient.getApiService().getFriendRequests().enqueue(new Callback<List<GetUserResponse>>() {
            @Override
            public void onResponse(Call<List<GetUserResponse>> call, Response<List<GetUserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setRequests(response.body());
                } else {
                    Log.e("FriendRequest", "조회 실패 코드: " + response.code());
                    Log.e("FriendRequest", "조회 실패 메시지: " + response.message());
                    try {
                        Log.e("FriendRequest", "에러 바디: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "친구 요청 불러오기 실패", Toast.LENGTH_SHORT).show();
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
                homeTitle.setText("친구 요청");
            }
        }
    }
}
