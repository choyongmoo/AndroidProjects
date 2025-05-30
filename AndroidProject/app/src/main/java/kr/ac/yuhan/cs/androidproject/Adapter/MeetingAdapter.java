package kr.ac.yuhan.cs.androidproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.yuhan.cs.androidproject.dto.Meeting;
import kr.ac.yuhan.cs.androidproject.R;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private final List<Meeting> meetingList;

    public MeetingAdapter(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_meeting.xml 레이아웃을 inflate 해서 뷰 홀더 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        // position에 맞는 Meeting 데이터 가져오기
        Meeting meeting = meetingList.get(position);
        holder.tvMeetingTitle.setText(meeting.getTitle());
        holder.tvMeetingTime.setText(meeting.getTime());
        holder.tvMeetingLocation.setText(meeting.getLocation());
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView tvMeetingTitle;
        TextView tvMeetingTime;
        TextView tvMeetingLocation;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMeetingTitle = itemView.findViewById(R.id.tvMeetingTitle);
            tvMeetingTime = itemView.findViewById(R.id.tvMeetingTime);
            tvMeetingLocation = itemView.findViewById(R.id.tvMeetingLocation);
        }
    }
}
