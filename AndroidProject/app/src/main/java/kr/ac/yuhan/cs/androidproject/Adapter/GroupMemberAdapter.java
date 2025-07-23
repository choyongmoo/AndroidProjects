package kr.ac.yuhan.cs.androidproject.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.dto.GroupMember;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.MemberViewHolder> {

    private List<GroupMember> memberList;

    public GroupMemberAdapter(List<GroupMember> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        GroupMember member = memberList.get(position);
        Log.d("GroupMemberAdapter", "onBindViewHolder 멤버 이름: " + member.getUserName());
        holder.tvMemberName.setText(member.getUserName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
        }
    }

    public void updateMembers(List<GroupMember> newMembers) {
        this.memberList.clear();
        this.memberList.addAll(newMembers);
        notifyDataSetChanged();
    }
}
