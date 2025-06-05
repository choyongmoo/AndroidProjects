package kr.ac.yuhan.cs.androidproject.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentRequest;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMeetingFragment extends Fragment {

    private EditText etTitle, etPenalty;
    private MaterialButton btnDate, btnTime, btnSelectPlace;
    private RadioGroup rgRewardType;
    private MaterialButton confirmBtn, cancelBtn;
    private Spinner spinnerGroup;

    private long selectedGroupId = 0;
    private long selectedPlaceId = 1L; // TODO 자준아 지도 기능 합쳐

    private List<GroupSummary> groupList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_meeting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 뷰 초기화
        etTitle = view.findViewById(R.id.etTitle);
        btnDate = view.findViewById(R.id.btnDate);
        btnTime = view.findViewById(R.id.btnTime);
        btnSelectPlace = view.findViewById(R.id.btnSelectPlace);
        etPenalty = view.findViewById(R.id.etPenalty);
        rgRewardType = view.findViewById(R.id.rgReward);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        spinnerGroup = view.findViewById(R.id.spinnerGroup);

        // 날짜, 시간 버튼 클릭 이벤트
        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());

        // 확인 버튼 클릭 이벤트
        confirmBtn.setOnClickListener(v -> createAppointment());

        cancelBtn.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
        });

        loadGroups();
    }

    private void loadGroups() {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getGroups().enqueue(new Callback<List<GroupSummary>>() {
            @Override
            public void onResponse(Call<List<GroupSummary>> call, Response<List<GroupSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList = response.body();

                    List<String> groupNames = new ArrayList<>();
                    for (GroupSummary group : groupList) {
                        groupNames.add(group.getGroupName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_spinner_item,
                            groupNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerGroup.setAdapter(adapter);

                    spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            GroupSummary selectedGroup = groupList.get(position);
                            selectedGroupId = selectedGroup.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "그룹 목록 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupSummary>> call, Throwable t) {
                Toast.makeText(getContext(), "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    btnDate.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    btnTime.setText(formattedTime);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void createAppointment() {
        String title = etTitle.getText().toString().trim();
        String date = btnDate.getText().toString().trim();
        String time = btnTime.getText().toString().trim();
        String place = btnSelectPlace.getText().toString().trim();
        String penaltyStr = etPenalty.getText().toString().trim();

        // 필수 입력값 체크
        if (title.isEmpty() || date.isEmpty() || time.isEmpty() || place.isEmpty()) {
            Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int penalty = 0;
        try {
            penalty = Integer.parseInt(penaltyStr);
        } catch (NumberFormatException e) {
            penalty = 0;
        }

        int selectedRewardId = rgRewardType.getCheckedRadioButtonId();
        if (selectedRewardId == R.id.rbNone) {
            penalty = 0;
        }

        String dateTimeStr = date + "T" + time + ":00";

        try {
            org.threeten.bp.LocalDateTime.parse(dateTimeStr, org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            Toast.makeText(getContext(), "날짜 또는 시간 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentRequest request = new AppointmentRequest();
        request.setTitle(title);
        request.setPenalty(penalty);
        request.setGroupId(selectedGroupId);
        request.setPlaceId(selectedPlaceId);
        request.setTime(dateTimeStr);

        ApiService apiService = RetrofitClient.getApiService();

        apiService.createAppointment(request).enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "약속 생성 완료!", Toast.LENGTH_SHORT).show();
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "약속 생성 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponse> call, Throwable t) {
                Toast.makeText(getContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("스케줄 생성");
            }
        }
    }
}
