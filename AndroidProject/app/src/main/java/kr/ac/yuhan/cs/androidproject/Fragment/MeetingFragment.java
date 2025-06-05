package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.ac.yuhan.cs.androidproject.Adapter.MeetingAdapter;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentResponse;
import kr.ac.yuhan.cs.androidproject.dto.Meeting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingFragment extends Fragment {

    private RecyclerView rvMeetingList;
    private MeetingAdapter adapter;
    private final List<Meeting> meetingList = new ArrayList<>();

    public MeetingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meeting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMeetingList = view.findViewById(R.id.rvMeetingList);
        adapter = new MeetingAdapter(meetingList);
        rvMeetingList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMeetingList.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Meeting clickedMeeting = meetingList.get(position);
            openDetailFragment(clickedMeeting);
        });

        MaterialButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        });

        loadMeetingsFromServer();
    }

    private void openDetailFragment(Meeting meeting) {
        Bundle bundle = new Bundle();
        bundle.putLong("appointmentId", meeting.getId());
        bundle.putString("title", meeting.getTitle());
        bundle.putString("time", meeting.getTime());
        bundle.putString("location", meeting.getLocation());
        bundle.putString("groupId", meeting.getGroupId());
        bundle.putInt("penalty", meeting.getPenalty());

        DetailMeetingFragment detailFragment = new DetailMeetingFragment();
        detailFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadMeetingsFromServer() {
        RetrofitClient.getApiService().getMyAppointments()
                .enqueue(new Callback<List<AppointmentResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AppointmentResponse>> call,
                                           @NonNull Response<List<AppointmentResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            meetingList.clear();
                            for (AppointmentResponse appointment : response.body()) {
                                meetingList.add(new Meeting(
                                        appointment.getId(),
                                        appointment.getTitle(),
                                        formatDateTime(appointment.getTime()),
                                        appointment.getPlaceId() != null ? "임시 장소" : "장소 없음",
                                        String.valueOf(appointment.getGroupId()),
                                        appointment.getPenalty() != null ? appointment.getPenalty() : 0
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "서버에서 약속을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AppointmentResponse>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDateTime(String rawDateTime) {
        try {
            SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
            Date date = serverFormat.parse(rawDateTime);

            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy년 M월 d일 a h:mm", Locale.KOREA);
            return displayFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return rawDateTime;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("약속 목록");
            }
        }
    }
}
