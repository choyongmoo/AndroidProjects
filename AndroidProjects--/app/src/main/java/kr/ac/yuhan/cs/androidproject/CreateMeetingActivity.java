package kr.ac.yuhan.cs.androidproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

// 약속 생성 화면 Activity (UI만 구현)
public class CreateMeetingActivity extends AppCompatActivity {

    private Button btnDate, btnTime, btnCreate;
    private EditText etTitle, etLocation;
    private RadioGroup rgReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting); // create_meeting.xml 연결

        // XML 요소 연결
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnCreate = findViewById(R.id.btnCreate);
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        rgReward = findViewById(R.id.rgReward);

        Calendar calendar = Calendar.getInstance();

        // ✅ 날짜 선택
        btnDate.setOnClickListener(v -> {
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                btnDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }, y, m, d);
            dialog.show();
        });

        // ✅ 시간 선택
        btnTime.setOnClickListener(v -> {
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                btnTime.setText(String.format("%02d:%02d", hourOfDay, minute));
            }, h, min, true);
            dialog.show();
        });

        // ✅ 장소 입력칸 클릭 시 단순 안내 메시지
        etLocation.setOnClickListener(v -> {
            Toast.makeText(this, "지도에서 위치 선택 예정입니다", Toast.LENGTH_SHORT).show();
        });

        // ✅ 약속 생성 버튼 클릭 시 입력값 확인 후 Toast 출력
        btnCreate.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String date = btnDate.getText().toString().trim();
            String time = btnTime.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "약속 생성 완료 (UI 시뮬레이션)", Toast.LENGTH_SHORT).show();
            finish(); // 임시로 화면 종료
        });
    }
}
