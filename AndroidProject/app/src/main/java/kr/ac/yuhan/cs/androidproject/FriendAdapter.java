package kr.ac.yuhan.cs.androidproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<GetUserResponse> friends = new ArrayList<>();

    public void setFriends(List<GetUserResponse> friends) {
        this.friends = friends;
        notifyDataSetChanged();
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
        String name = friend.getDisplayName();
        Log.d("FriendAdapter", "position: " + position + ", friend: " + friend.toString());
        holder.textDisplayName.setText(friend.getDisplayName());

        // Glide로 프로필 이미지 URL 불러오기 (default_profile은 기본 이미지)
        Glide.with(holder.imageProfile.getContext())
                .load(friend.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(holder.imageProfile);
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
