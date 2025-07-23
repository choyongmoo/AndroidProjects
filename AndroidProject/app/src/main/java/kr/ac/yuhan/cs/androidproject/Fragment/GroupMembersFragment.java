package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.Adapter.GroupMemberAdapter;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.dto.GroupDetail;
import kr.ac.yuhan.cs.androidproject.dto.GroupMember;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMembersFragment extends Fragment {

    private static final String ARG_GROUP_ID = "group_id";

    private String groupName;
    private GroupMemberAdapter adapter;

    public static GroupMembersFragment newInstance(long groupId) {
        GroupMembersFragment fragment = new GroupMembersFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_members, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerMembers = view.findViewById(R.id.recyclerGroupMembers);
        recyclerMembers.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new GroupMemberAdapter(new ArrayList<>());
        recyclerMembers.setAdapter(adapter);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Fragment groupFragment = new GroupFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, groupFragment)
                    .addToBackStack(null)
                    .commit();
        });

        Button btnMakeAppointment = view.findViewById(R.id.btnMakeAppointment);
        btnMakeAppointment.setOnClickListener(v -> {
            Fragment createMeetingFragment = new CreateMeetingFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, createMeetingFragment)
                    .addToBackStack(null)
                    .commit();
        });

        if (getArguments() != null) {
            long groupId = getArguments().getLong(ARG_GROUP_ID);
            fetchGroupDetail(groupId);
        }
    }

    private void fetchGroupDetail(long groupId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<GroupDetail> call = apiService.getGroupDetail(groupId);

        call.enqueue(new Callback<GroupDetail>() {
            @Override
            public void onResponse(Call<GroupDetail> call, Response<GroupDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupDetail groupDetail = response.body();
                    groupName = groupDetail.getGroupName();

                    TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
                    if (homeTitle != null) {
                        homeTitle.setText(groupName + " 멤버");
                    }

                    List<GroupMember> members = groupDetail.getMembers();
                    adapter.updateMembers(members);

                } else {
                    Toast.makeText(getContext(), "그룹 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupDetail> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null && groupName != null) {
                homeTitle.setText(groupName + " 멤버");
            }
        }
    }
}
