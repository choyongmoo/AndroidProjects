package kr.ac.yuhan.cs.androidproject.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.AddGroupMemberRequest;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<GetUserResponse> friends = new ArrayList<>();
    private List<GroupSummary> groups = new ArrayList<>();

    public void setFriends(List<GetUserResponse> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public void setGroups(List<GroupSummary> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        GetUserResponse friend = friends.get(position);
        holder.textDisplayName.setText(friend.getDisplayName());

        Glide.with(holder.imageProfile.getContext())
                .load(friend.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(holder.imageProfile);

        holder.itemView.setOnClickListener(v -> showFriendDialog(v, friend));
    }

    private void showFriendDialog(View view, GetUserResponse friend) {
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_friend_profile, null);

        ImageView imageProfileDialog = dialogView.findViewById(R.id.imageProfileDialog);
        TextView textDisplayNameDialog = dialogView.findViewById(R.id.textDisplayNameDialog);
        Button btnInviteGroup = dialogView.findViewById(R.id.btnInviteGroup);

        textDisplayNameDialog.setText(friend.getDisplayName());
        Glide.with(view.getContext())
                .load(friend.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(imageProfileDialog);

        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setView(dialogView)
                .create();

        btnInviteGroup.setOnClickListener(v -> {
            dialog.dismiss();

            final EditText input = new EditText(view.getContext());
            input.setHint("그룹 이름을 입력하세요");

            new AlertDialog.Builder(view.getContext())
                    .setTitle("그룹 초대")
                    .setMessage(friend.getDisplayName() + "님을 초대할 그룹 이름을 입력하세요")
                    .setView(input)
                    .setPositiveButton("초대", (dialogInterface, which) -> {
                        String groupName = input.getText().toString().trim();
                        if (!groupName.isEmpty()) {
                            long groupId = findGroupIdByName(groupName);
                            if (groupId == -1) {
                                Toast.makeText(view.getContext(), "존재하지 않는 그룹 이름입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            AddGroupMemberRequest request = new AddGroupMemberRequest(friend.getUsername());

                            RetrofitClient.getApiService().addGroupMember(groupId, request)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(view.getContext(),
                                                        friend.getDisplayName() + "님을 그룹에 초대했습니다!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(view.getContext(),
                                                        "초대 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(view.getContext(),
                                                    "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(view.getContext(), "그룹 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });

        dialog.show();
    }

    private long findGroupIdByName(String groupName) {
        for (GroupSummary group : groups) {
            if (group.getGroupName().equalsIgnoreCase(groupName)) {
                return group.getId();
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textDisplayName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textDisplayName = itemView.findViewById(R.id.textDisplayName);
        }
    }
}
