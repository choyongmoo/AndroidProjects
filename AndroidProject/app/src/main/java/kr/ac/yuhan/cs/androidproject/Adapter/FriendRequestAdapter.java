package kr.ac.yuhan.cs.androidproject.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.dto.FriendAcceptDto;
import kr.ac.yuhan.cs.androidproject.dto.FriendRequestDto;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestViewHolder> {

    private List<GetUserResponse> requests = new ArrayList<>();
    private ApiService apiService;

    public FriendRequestAdapter(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setRequests(List<GetUserResponse> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        GetUserResponse user = requests.get(position);
        holder.textUsername.setText(user.getUsername());

        Glide.with(holder.imageProfile.getContext())
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(holder.imageProfile);

        holder.btnAccept.setOnClickListener(v -> {
            apiService.acceptFriendRequest(new FriendAcceptDto(user.getUsername())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "친구 요청 수락 완료", Toast.LENGTH_SHORT).show();
                        // 요청 리스트에서 삭제 후 갱신
                        requests.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, requests.size());
                    } else {
                        Log.e("FriendAccept", "수락 실패 코드: " + response.code());
                        Log.e("FriendAccept", "수락 실패 메시지: " + response.message());
                        try {
                            if (response.errorBody() != null) {
                                Log.e("FriendAccept", "에러 바디: " + response.errorBody().string());
                            }
                        } catch (Exception e) {
                            Log.e("FriendAccept", "에러 바디 읽기 실패", e);
                        }
                        Toast.makeText(holder.itemView.getContext(), "수락 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btnReject.setOnClickListener(v -> {
            apiService.rejectFriendRequest(new FriendRequestDto(user.getUsername())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "친구 요청 거절 완료", Toast.LENGTH_SHORT).show();
                        requests.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, requests.size());
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "거절 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textUsername;
        Button btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textUsername = itemView.findViewById(R.id.textUsername);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
