package kr.ac.yuhan.cs.androidproject.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentRequest;
import kr.ac.yuhan.cs.androidproject.dto.AppointmentResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupSummary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAppointmentFragment extends Fragment {

    private EditText etTitle, etPenalty;
    private Spinner spinnerGroup;
    private MaterialButton btnDate, btnTime, btnSelectPlace, confirmBtn, cancelBtn;
    private RadioGroup rgReward;
    private RadioButton rbWinnerTakesAll, rbSplit, rbNone;

    private Calendar selectedDateTime = Calendar.getInstance();
    private Long appointmentId;
    private AppointmentResponse currentAppointment;

    private long initialGroupId = -1L;
    private List<GroupSummary> groupList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_meeting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();

        if (getArguments() != null) {
            if (getArguments().containsKey("appointmentId")) {
                appointmentId = getArguments().getLong("appointmentId", -1L);
            }
            if (getArguments().containsKey("groupId")) {
                initialGroupId = getArguments().getLong("groupId", -1L);
            }
        }

        fetchAppointmentDetails(appointmentId);
        loadGroupList();
    }

    private void initViews(View view) {
        etTitle = view.findViewById(R.id.etTitle);
        etPenalty = view.findViewById(R.id.etPenalty);
        spinnerGroup = view.findViewById(R.id.spinnerGroup);
        btnDate = view.findViewById(R.id.btnDate);
        btnTime = view.findViewById(R.id.btnTime);
        btnSelectPlace = view.findViewById(R.id.btnSelectPlace);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        rgReward = view.findViewById(R.id.rgReward);
        rbWinnerTakesAll = view.findViewById(R.id.rbWinnerTakesAll);
        rbSplit = view.findViewById(R.id.rbSplit);
        rbNone = view.findViewById(R.id.rbNone);
    }

    private void setupListeners() {
        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());

        confirmBtn.setOnClickListener(v -> {
            if (currentAppointment != null) {
                updateAppointment();
            } else {
                Toast.makeText(requireContext(), "기존 데이터를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void fetchAppointmentDetails(Long id) {
        if (id == null || id == -1L) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getAppointment(id).enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentAppointment = response.body();
                    populateUI(currentAppointment);

                    if (!groupList.isEmpty()) {
                        selectGroupSpinnerById(Long.parseLong(currentAppointment.getGroupId()));
                    }
                } else {
                    Toast.makeText(requireContext(), "약속 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(AppointmentResponse appointment) {
        etTitle.setText(appointment.getTitle());
        etPenalty.setText(appointment.getPenalty() != null ? String.valueOf(appointment.getPenalty()) : "0");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(appointment.getTime());
            if (date != null) {
                selectedDateTime.setTime(date);
                btnDate.setText(new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(date));
                btnTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date));
            }
        } catch (ParseException ignored) {}
        rbNone.setChecked(true);
    }

    private void loadGroupList() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getGroups().enqueue(new Callback<List<GroupSummary>>() {
            @Override
            public void onResponse(Call<List<GroupSummary>> call, Response<List<GroupSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList = response.body();
                    setupGroupSpinner();

                    if (initialGroupId != -1L) {
                        selectGroupSpinnerById(initialGroupId);
                    } else if (currentAppointment != null) {
                        selectGroupSpinnerById(Long.parseLong(currentAppointment.getGroupId()));
                    }
                } else {
                    Toast.makeText(requireContext(), "그룹 목록을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupSummary>> call, Throwable t) {
                Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupGroupSpinner() {
        List<String> groupNames = new ArrayList<>();
        for (GroupSummary group : groupList) {
            groupNames.add(group.getGroupName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, groupNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);
    }

    private void selectGroupSpinnerById(long groupId) {
        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).getId() == groupId) {
                spinnerGroup.setSelection(i);
                return;
            }
        }
    }

    private void updateAppointment() {
        String title = etTitle.getText().toString().trim();
        int penalty = 0;
        try {
            penalty = Integer.parseInt(etPenalty.getText().toString().trim());
        } catch (NumberFormatException ignored) {}

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String formattedTime = format.format(selectedDateTime.getTime());

        long selectedGroupId = groupList.isEmpty() ? -1L : groupList.get(spinnerGroup.getSelectedItemPosition()).getId();
        long placeId = 1L;

        if (selectedGroupId == -1L) {
            Toast.makeText(requireContext(), "그룹을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentRequest request = new AppointmentRequest(title, penalty, selectedGroupId, placeId, formattedTime);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.updateAppointment(appointmentId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "약속이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new MeetingFragment())
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(requireContext(), "수정 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedDateTime.set(year1, month1, dayOfMonth);
                    btnDate.setText(String.format(Locale.getDefault(), "%d년 %02d월 %02d일", year1, month1 + 1, dayOfMonth));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute1);
                    btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1));
                }, hour, minute, true);
        timePickerDialog.show();
    }
}
