package kr.ac.yuhan.cs.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// RecyclerView에 연결해주는 어댑터 클래스
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private ArrayList<Meeting> meetings; // 약속 데이터 리스트

    // 생성자: 외부에서 데이터 리스트를 받아옴
    public MeetingAdapter(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    // 아이템 한 줄의 레이아웃을 연결해주는 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDateTime, tvLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }

    // 레이아웃을 생성하는 부분 (item_meeting.xml 연결)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    // 실제 데이터를 각 카드뷰에 표시하는 부분
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meeting item = meetings.get(position);
        holder.tvTitle.setText(item.title);
        holder.tvDateTime.setText(item.dateTime);
        holder.tvLocation.setText(item.location);
    }

    // 리스트에 몇 개의 항목이 있는지 반환
    @Override
    public int getItemCount() {
        return meetings.size();
    }
}
