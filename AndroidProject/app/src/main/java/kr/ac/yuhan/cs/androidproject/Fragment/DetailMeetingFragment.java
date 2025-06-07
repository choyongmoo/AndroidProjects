package kr.ac.yuhan.cs.androidproject.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.GroupDetail;
import kr.ac.yuhan.cs.androidproject.dto.GroupMember;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMeetingFragment extends Fragment {

    private TextView tvTitle, tvDateTime, tvPenalty, tvGroup, tvPlace;
    private LinearLayout participantContainer;
    private Button btnPlaceView, btnEditInviteDelete, btnBack;

    public DetailMeetingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_meeting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvPenalty = view.findViewById(R.id.tvPenalty);
        tvGroup = view.findViewById(R.id.tvGroup);
        tvPlace = view.findViewById(R.id.tvPlace);
        participantContainer = view.findViewById(R.id.participantContainer);

        btnPlaceView = view.findViewById(R.id.btnPlaceView);
        btnEditInviteDelete = view.findViewById(R.id.btnEditInviteDelete);
        btnBack = view.findViewById(R.id.btnBack);

        btnPlaceView.setOnClickListener(v -> {
            double lat = getArguments() != null ? getArguments().getDouble("latitude", 0.0) : 0.0;
            double lng = getArguments() != null ? getArguments().getDouble("longitude", 0.0) : 0.0;
            String placeName = tvPlace.getText().toString().replace("장소: ", "");

            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", lat);
            bundle.putDouble("longitude", lng);
            bundle.putString("placeName", placeName);


            PlaceViewFragment placeViewFragment = new PlaceViewFragment();
            placeViewFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, placeViewFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnEditInviteDelete.setOnClickListener(v -> {
            String[] options = {"수정", "초대", "삭제", "취소"};

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("무엇을 하시겠습니까?")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0: // 수정
                                Bundle bundle = new Bundle();
                                bundle.putLong("appointmentId", getArguments() != null ? getArguments().getLong("appointmentId", -1L) : -1L);
                                bundle.putString("title", tvTitle.getText().toString());
                                bundle.putString("time", tvDateTime.getText().toString());
                                bundle.putString("place", tvPlace.getText().toString().replace("장소: ", ""));
                                bundle.putInt("penalty", Integer.parseInt(tvPenalty.getText().toString().replace("벌금: ", "").replace("원", "").replace(",", "")));
                                bundle.putLong("groupId", getArguments() != null ? getArguments().getLong("groupId", -1L) : -1L);
                                bundle.putLong("placeId", getArguments() != null ? getArguments().getLong("placeId", -1L) : -1L);

                                EditAppointmentFragment editFragment = new EditAppointmentFragment();
                                editFragment.setArguments(bundle);

                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, editFragment)
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 1: // 초대
                                FriendListFragment friendListFragment = new FriendListFragment();
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, friendListFragment)
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 2: // 삭제
                                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                        .setTitle("정말 삭제하시겠습니까?")
                                        .setMessage("삭제하면 이 약속은 되돌릴 수 없습니다.")
                                        .setPositiveButton("삭제", (dialogInterface, i) -> {
                                            Long appointmentId = getArguments() != null ? getArguments().getLong("appointmentId", -1L) : -1L;
                                            if (appointmentId == -1L) {
                                                Toast.makeText(getContext(), "약속 ID가 없습니다", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            ApiService apiService = RetrofitClient.getApiService();
                                            apiService.deleteAppointment(appointmentId).enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(getContext(), "약속이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                                                        requireActivity().getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.fragment_container, new MeetingFragment())
                                                                .commit();
                                                    } else {
                                                        Toast.makeText(getContext(), "삭제 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        })
                                        .setNegativeButton("취소", null)
                                        .show();
                                break;
                            case 3: // 취소
                                dialog.dismiss();
                                break;
                        }
                    })
                    .show();
        });

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MeetingFragment())
                    .commit();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title", "제목 없음");
            String dateTime = bundle.getString("time", "시간 없음");
            String place = bundle.getString("place", "장소 없음");

            int penalty = bundle.getInt("penalty", -1);
            long groupId = -1L;

            if (bundle.containsKey("groupId")) {
                try {
                    groupId = Long.parseLong(bundle.getString("groupId"));
                } catch (NumberFormatException e) {
                    Log.e("DetailMeetingFragment", "groupId parsing failed", e);
                }
            }

            tvTitle.setText(title);
            tvDateTime.setText(dateTime);
            tvPlace.setText("장소: " + place);

            if (penalty >= 0) {
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.KOREA);
                String formattedPenalty = nf.format(penalty) + "원";
                tvPenalty.setText("벌금: " + formattedPenalty);
            } else {
                tvPenalty.setText("벌금: 알 수 없음");
            }

            if (groupId != -1L) {
                loadGroupInfoAndMembers(groupId);
            } else {
                tvGroup.setText("그룹: 알 수 없음");
                showNoParticipants();
            }

        } else {
            Toast.makeText(getContext(), "상세 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGroupInfoAndMembers(long groupId) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getGroupDetail(groupId).enqueue(new Callback<GroupDetail>() {
            @Override
            public void onResponse(Call<GroupDetail> call, Response<GroupDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupDetail groupDetail = response.body();
                    tvGroup.setText("그룹: " + groupDetail.getGroupName());

                    List<GroupMember> members = groupDetail.getMembers();
                    participantContainer.removeAllViews();

                    if (members != null && !members.isEmpty()) {
                        for (GroupMember member : members) {
                            String username = member.getUserName();

                            apiService.getUserInfo(username).enqueue(new Callback<GetUserResponse>() {
                                @Override
                                public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String displayName = response.body().getDisplayName();
                                        TextView tv = new TextView(getContext());
                                        tv.setText(displayName);
                                        tv.setTextSize(16);
                                        participantContainer.addView(tv);
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetUserResponse> call, Throwable t) {
                                    Log.e("DetailMeetingFragment", "getUserInfo 실패", t);
                                }
                            });
                        }
                    } else {
                        showNoParticipants();
                    }

                } else {
                    tvGroup.setText("그룹: 불러오기 실패");
                    showNoParticipants();
                }
            }

            @Override
            public void onFailure(Call<GroupDetail> call, Throwable t) {
                Log.e("DetailMeetingFragment", "getGroupDetail 실패", t);
                tvGroup.setText("그룹: 불러오기 실패");
                showNoParticipants();
            }
        });
    }

    private void showNoParticipants() {
        participantContainer.removeAllViews();
        TextView tv = new TextView(getContext());
        tv.setText("참여자가 없습니다.");
        tv.setTextSize(16);
        participantContainer.addView(tv);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            TextView homeTitle = getActivity().findViewById(R.id.homeTitle);
            if (homeTitle != null) {
                homeTitle.setText("약속 상세 내역");
            }
        }
    }
}

