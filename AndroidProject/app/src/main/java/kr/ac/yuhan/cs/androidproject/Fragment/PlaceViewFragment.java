package kr.ac.yuhan.cs.androidproject.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.TokenManager;
import kr.ac.yuhan.cs.androidproject.dto.SharedLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 실제 위치 연동 시 필요
// import com.google.android.gms.location.FusedLocationProviderClient;
// import com.google.android.gms.location.LocationServices;


public class PlaceViewFragment extends Fragment implements OnMapReadyCallback {

    private double latitude = 0.0;
    private double longitude = 0.0;
    private String placeName = "약속 장소";
    private long appointmentId = -1;

    private GoogleMap mMap;
    private boolean isSharing = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private String myUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TokenManager tokenManager = new TokenManager(requireContext());
        myUserId = tokenManager.getUserIdFromToken();

        if (getArguments() != null) {
            latitude = getArguments().getDouble("latitude", 0.0);
            longitude = getArguments().getDouble("longitude", 0.0);
            placeName = getArguments().getString("placeName", "약속 장소");
            appointmentId = getArguments().getLong("appointmentId", -1);
        }

        Log.d("MAP", "초기 장소: " + latitude + ", " + longitude + " / appointmentId: " + appointmentId);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        Button btnShare = view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> toggleLocationSharing());
    }

    private void toggleLocationSharing() {
        isSharing = !isSharing;
        if (isSharing) {
            startSharingLocation();
        } else {
            stopSharingLocation();
        }
    }

    private void startSharingLocation() {
        handler.post(fakeLocationUpdateRunnable);
    }

    private void stopSharingLocation() {
        handler.removeCallbacks(fakeLocationUpdateRunnable);
        // 공유 해제 시 위치 업로드 isSharing false 처리
        uploadLocation(null);
    }

    private final Runnable fakeLocationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            double latOffset = (new Random().nextDouble() - 0.5) / 500;
            double lngOffset = (new Random().nextDouble() - 0.5) / 500;
            Location fakeLocation = new Location("default");
            fakeLocation.setLatitude(latitude + latOffset);
            fakeLocation.setLongitude(longitude + lngOffset);

            // 실제 위치 연동하려면 아래 주석 해제
            /*
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    fakeLocation.setLatitude(location.getLatitude());
                    fakeLocation.setLongitude(location.getLongitude());
                }
            });
            */


            Log.d("SHARE", "업로드 위치: " + fakeLocation.getLatitude() + ", " + fakeLocation.getLongitude());
            uploadLocation(fakeLocation);
            handler.postDelayed(this, 60000); // 1분마다 업데이트
        }
    };

    private void uploadLocation(@Nullable Location location) {
        if (myUserId == null) return;

        ApiService apiService = RetrofitClient.getApiService();

        double lat = (location != null) ? location.getLatitude() : latitude;
        double lng = (location != null) ? location.getLongitude() : longitude;
        boolean sharingFlag = (location != null);

        SharedLocation loc = new SharedLocation(myUserId, appointmentId, lat, lng, sharingFlag);

        apiService.uploadSharedLocation(loc).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("SHARE", "위치 업로드 성공");
                loadSharedMarkers();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("SHARE", "위치 업로드 실패", t);
            }
        });
    }

    private void loadSharedMarkers() {
        if (mMap == null) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getSharedLocations(appointmentId).enqueue(new Callback<List<SharedLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<SharedLocation>> call, @NonNull Response<List<SharedLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMap.clear();

                    float[] hues = {
                            BitmapDescriptorFactory.HUE_GREEN,
                            BitmapDescriptorFactory.HUE_BLUE,
                            BitmapDescriptorFactory.HUE_ORANGE,
                            BitmapDescriptorFactory.HUE_VIOLET,
                            BitmapDescriptorFactory.HUE_CYAN
                    };

                    Map<String, Float> userColorMap = new HashMap<>();
                    int colorIndex = 0;

                    for (SharedLocation loc : response.body()) {
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                        if (!userColorMap.containsKey(loc.getUserId())) {
                            userColorMap.put(loc.getUserId(), hues[colorIndex % hues.length]);
                            colorIndex++;
                        }

                        float color = userColorMap.get(loc.getUserId());

                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(loc.getUserId())
                                .icon(BitmapDescriptorFactory.defaultMarker(color));
                        mMap.addMarker(options);

                        if (loc.getUserId().equals(myUserId)) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        }
                    }

                    // 약속 장소 마커 (빨간색)
                    LatLng mainLoc = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(mainLoc)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SharedLocation>> call, @NonNull Throwable t) {
                Log.e("SHARE", "마커 불러오기 실패", t);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mainLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(mainLoc).title(placeName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainLoc, 15));
        loadSharedMarkers();
    }
}

