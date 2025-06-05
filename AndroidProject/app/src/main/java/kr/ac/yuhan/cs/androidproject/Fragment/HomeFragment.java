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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvNextMeetingTitle, tvNextMeetingTime, tvNextMeetingPlace;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        tvNextMeetingTitle = rootView.findViewById(R.id.tvMeetingTitle);
        tvNextMeetingTime = rootView.findViewById(R.id.tvMeetingTime);
        tvNextMeetingPlace = rootView.findViewById(R.id.tvMeetingLocation);

        MaterialButton btnCreateMeeting = rootView.findViewById(R.id.btnCreateMeeting);
        btnCreateMeeting.setOnClickListener(v -> {
            CreateMeetingFragment createMeetingFragment = new CreateMeetingFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, createMeetingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        MaterialButton btnGroupManage = rootView.findViewById(R.id.btnGroupManage);
        btnGroupManage.setOnClickListener(v -> {
            GroupFragment groupFragment = new GroupFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, groupFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        MaterialButton btnMeetingList = rootView.findViewById(R.id.btnMeetingList);
        btnMeetingList.setOnClickListener(v -> {
            MeetingFragment meetingFragment = new MeetingFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, meetingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        fetchAndDisplayEarliestAppointment();

        return rootView;
    }

    private void fetchAndDisplayEarliestAppointment() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getMyAppointments().enqueue(new Callback<List<AppointmentResponse>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponse>> call, Response<List<AppointmentResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AppointmentResponse> appointments = response.body();
                    AppointmentResponse earliest = findEarliestAppointment(appointments);
                    if (earliest != null) {
                        displayAppointment(earliest);
                    } else {
                        clearAppointmentDisplay();
                    }
                } else {
                    Toast.makeText(getContext(), "약속 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    clearAppointmentDisplay();
                }
            }

            @Override
            public void onFailure(Call<List<AppointmentResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                clearAppointmentDisplay();
            }
        });
    }

    private AppointmentResponse findEarliestAppointment(List<AppointmentResponse> appointments) {
        if (appointments == null || appointments.isEmpty()) return null;

        AppointmentResponse earliest = null;
        for (AppointmentResponse app : appointments) {
            if (earliest == null || isEarlier(app, earliest)) {
                earliest = app;
            }
        }
        return earliest;
    }

    private boolean isEarlier(AppointmentResponse a, AppointmentResponse b) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date aDate = sdf.parse(a.getTime());
            Date bDate = sdf.parse(b.getTime());
            return aDate.before(bDate);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private void displayAppointment(AppointmentResponse appointment) {
        tvNextMeetingTitle.setText("제목: " + appointment.getTitle());

        String formattedTime = formatDateTime(appointment.getTime());
        tvNextMeetingTime.setText("시간: " + formattedTime);

        // 장소 정보가 placeId로만 있음 -> 실제 장소 이름을 보여주려면 placeId -> 장소 이름 매핑 필요
        tvNextMeetingPlace.setText("장소: " + appointment.getPlaceId());
    }

    private String formatDateTime(String dateTimeStr) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
            Date date = sdfInput.parse(dateTimeStr);
            return sdfOutput.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateTimeStr;
        }
    }

    private void clearAppointmentDisplay() {
        tvNextMeetingTitle.setText("약속이 없습니다.");
        tvNextMeetingTime.setText("");
        tvNextMeetingPlace.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("홈 화면");
            }
        }
    }
}
