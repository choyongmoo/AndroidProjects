package kr.ac.yuhan.cs.androidproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.io.IOException;
import java.io.InputStream;

import kr.ac.yuhan.cs.androidproject.MainActivity;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.dto.GetUserResponse;
import kr.ac.yuhan.cs.androidproject.dto.UpdateUserRequest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {

    private Uri selectedImageUri = null;
    private ImageView profileImageView;
    private static final String SERVER_ROOT_URL = "http://10.0.2.2:8080";

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String yourAccessToken = "";

    public InfoFragment() {
        super(R.layout.info);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getContext();
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            yourAccessToken = prefs.getString("accessToken", "");
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Glide.with(this)
                                .load(selectedImageUri)
                                .placeholder(R.drawable.default_profile)
                                .into(profileImageView);
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();
        if (context == null) return;

        profileImageView = view.findViewById(R.id.userProfileImage1);
        TextView infoIdView = view.findViewById(R.id.infoIdView);
        EditText displayNameEdit = view.findViewById(R.id.infoEditDisplayName);
        EditText passwordEdit = view.findViewById(R.id.infoEditPwd);
        EditText emailEdit = view.findViewById(R.id.infoEditEmail);
        Button modifyBtn = view.findViewById(R.id.register_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        String username = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("username", "");

        profileImageView.setOnClickListener(v -> {
            String[] options = {"사진 선택", "기본 이미지로 변경"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("프로필 사진 변경")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            imagePickerLauncher.launch(intent);
                        } else if (which == 1) {
                            selectedImageUri = null; // 기본 이미지 사용 표시
                            profileImageView.setImageResource(R.drawable.default_profile);
                        }
                    })
                    .show();
        });

        RetrofitClient.getApiService().getUserInfo(username)
                .enqueue(new Callback<GetUserResponse>() {
                    @Override
                    public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                        if (!isAdded() || getContext() == null) return;

                        if (response.isSuccessful() && response.body() != null) {
                            GetUserResponse user = response.body();
                            infoIdView.setText(user.getUsername());
                            displayNameEdit.setText(user.getDisplayName());
                            emailEdit.setText(user.getEmail());
                            passwordEdit.setText("");

                            String profileImageUrl = user.getProfileImageUrl();
                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                if (profileImageUrl.startsWith("/")) {
                                    profileImageUrl = SERVER_ROOT_URL + profileImageUrl;
                                }

                                GlideUrl glideUrl = new GlideUrl(
                                        profileImageUrl,
                                        new LazyHeaders.Builder()
                                                .addHeader("Authorization", "Bearer " + yourAccessToken)
                                                .build()
                                );

                                Glide.with(requireContext())
                                        .load(glideUrl)
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .error(R.drawable.default_profile)
                                        .into(profileImageView);
                            }
                        } else {
                            Toast.makeText(getContext(), "사용자 정보 불러오기 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                            infoIdView.setText(username);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUserResponse> call, Throwable t) {
                        if (!isAdded() || getContext() == null) return;
                        Toast.makeText(getContext(), "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        infoIdView.setText(username);
                    }
                });

        modifyBtn.setOnClickListener(v -> {
            String displayName = displayNameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String email = emailEdit.getText().toString();

            UpdateUserRequest updateUserRequest = new UpdateUserRequest(email, password, displayName, null);

            new AlertDialog.Builder(requireContext())
                    .setTitle("프로필 수정")
                    .setMessage("정말로 수정하시겠습니까?")
                    .setPositiveButton("네", (dialog, which) -> {
                        if (selectedImageUri != null) {
                            uploadProfileImageAndUpdateUser(selectedImageUri, username, updateUserRequest);
                        } else {
                            updateUserRequest.setProfileImageUrl("");
                            updateUserInfo(username, updateUserRequest);                        }
                    })
                    .setNegativeButton("아니요", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        cancelBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        });
    }

    private void uploadProfileImageAndUpdateUser(Uri imageUri, String username, UpdateUserRequest updateUserRequest) {
        try {
            Context context = getContext();
            if (context == null) return;

            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Toast.makeText(context, "이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] bytes = new byte[inputStream.available()];
            int readCount = inputStream.read(bytes);
            inputStream.close();

            if (readCount <= 0) {
                Toast.makeText(context, "이미지 데이터를 읽을 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody requestFile = RequestBody.create(bytes, MediaType.parse("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "profile.jpg", requestFile);

            RetrofitClient.getApiService()
                    .uploadProfileImage(username, body)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!isAdded() || getContext() == null) return;

                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String uploadedImageUrl = response.body().string();
                                    if (uploadedImageUrl.startsWith("/")) {
                                        uploadedImageUrl = SERVER_ROOT_URL + uploadedImageUrl;
                                    }

                                    updateUserRequest.setProfileImageUrl(uploadedImageUrl);
                                    updateUserInfo(username, updateUserRequest);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "응답 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "프로필 이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            if (!isAdded() || getContext() == null) return;
                            Toast.makeText(getContext(), "업로드 중 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            if (isAdded()) {
                Toast.makeText(requireContext(), "이미지 처리 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserInfo(String username, UpdateUserRequest updateUserRequest) {
        RetrofitClient.getApiService()
                .updateUser(username, updateUserRequest)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "프로필 수정 완료", Toast.LENGTH_SHORT).show();
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).refreshUserInfo();
                            }
                            FragmentTransaction transaction = requireActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.fragment_container, new HomeFragment());
                            transaction.commit();
                        } else {
                            Toast.makeText(getContext(), "프로필 수정 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "프로필 수정 중 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
