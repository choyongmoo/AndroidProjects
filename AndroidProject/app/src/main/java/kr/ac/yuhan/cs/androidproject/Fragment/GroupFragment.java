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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.Adapter.GroupAdapter;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.CreateGroupRequest;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupFragment extends Fragment {

    private RecyclerView recyclerGroupList;
    private GroupAdapter adapter;
    private List<GroupSummary> groups = new ArrayList<>();

    public GroupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list, container, false);

        recyclerGroupList = view.findViewById(R.id.recyclerGroupList);
        recyclerGroupList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new GroupAdapter(groups, groupSummary -> {
            long groupId = groupSummary.getId();

            // 수정: GroupMembersFragment에 groupId 하나만 넘김
            GroupMembersFragment fragment = GroupMembersFragment.newInstance(groupId);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerGroupList.setAdapter(adapter);

        Button btnCreateGroup = view.findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(v -> showCreateGroupDialog());

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Fragment homeFragment = new HomeFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .addToBackStack(null)
                    .commit();
        });

        loadGroupsFromApi();

        return view;
    }

    private void showCreateGroupDialog() {
        final EditText input = new EditText(getContext());
        input.setHint("그룹 이름을 입력하세요");

        new AlertDialog.Builder(requireContext())
                .setTitle("새 그룹 만들기")
                .setView(input)
                .setPositiveButton("생성", (dialog, which) -> {
                    String groupName = input.getText().toString().trim();
                    if (!groupName.isEmpty()) {
                        createGroup(groupName);
                    } else {
                        Toast.makeText(getContext(), "그룹 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void createGroup(String groupName) {
        CreateGroupRequest request = new CreateGroupRequest(groupName);

        RetrofitClient.getApiService().createGroup(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    loadGroupsFromApi();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("GroupFragment", "그룹 생성 실패: " + response.code() + ", " + errorBody);
                        Toast.makeText(getContext(), "그룹 생성 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGroupsFromApi() {
        Call<List<GroupSummary>> call = RetrofitClient.getApiService().getGroups();
        call.enqueue(new Callback<List<GroupSummary>>() {
            @Override
            public void onResponse(Call<List<GroupSummary>> call, Response<List<GroupSummary>> response) {
                if(response.isSuccessful() && response.body() != null){
                    groups.clear();
                    groups.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "그룹 목록을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("GroupFragment", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GroupSummary>> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GroupFragment", "API 호출 실패", t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("그룹 관리");
            }
        }
    }
}
