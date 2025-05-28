package kr.ac.yuhan.cs.androidproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class CreateMeetingFragment extends Fragment {

    private EditText etLocation;
    private Button btnDate, btnTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_meeting, container, false);

        etLocation = view.findViewById(R.id.etLocation);
        btnDate = view.findViewById(R.id.btnDate);
        btnTime = view.findViewById(R.id.btnTime);

        // 지도 위치 선택 후 결과 받기
        getParentFragmentManager().setFragmentResultListener(
                "requestKey_select", this, (requestKey, bundle) -> {
                    String address = bundle.getString("bundleKey_address");
                    etLocation.setText(address);
                });

        // 지도 화면으로 이동
        etLocation.setOnClickListener(v -> {
            SelectLocationFragment frag = new SelectLocationFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .addToBackStack(null)
                    .commit();
        });

        // 날짜 선택
        btnDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view1, year, month, day) -> {
                String dateStr = year + "-" + (month + 1) + "-" + day;
                btnDate.setText(dateStr);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 시간 선택
        btnTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(requireContext(), (view1, hour, minute) -> {
                String timeStr = String.format("%02d:%02d", hour, minute);
                btnTime.setText(timeStr);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        // 뒤로가기
        view.findViewById(R.id.cancel_btn).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView title = requireActivity().findViewById(R.id.homeTitle);
        if (title != null) title.setText("약속 만들기");
    }
}
