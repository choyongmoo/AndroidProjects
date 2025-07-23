package kr.ac.yuhan.cs.androidproject.Fragment;

import android.Manifest; import android.content.pm.PackageManager; import android.location.Location; import android.os.Bundle; import android.util.Log; import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup; import android.widget.Button;

import androidx.annotation.NonNull; import androidx.annotation.Nullable; import androidx.core.app.ActivityCompat; import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient; import com.google.android.gms.location.LocationServices; import com.google.android.gms.maps.CameraUpdateFactory; import com.google.android.gms.maps.GoogleMap; import com.google.android.gms.maps.OnMapReadyCallback; import com.google.android.gms.maps.SupportMapFragment; import com.google.android.gms.maps.model.BitmapDescriptorFactory; import com.google.android.gms.maps.model.LatLng; import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap; import java.util.List; import java.util.Map;

import kr.ac.yuhan.cs.androidproject.ApiService; import kr.ac.yuhan.cs.androidproject.R; import kr.ac.yuhan.cs.androidproject.RetrofitClient; import kr.ac.yuhan.cs.androidproject.TokenManager; import kr.ac.yuhan.cs.androidproject.dto.SharedLocation; import retrofit2.Call; import retrofit2.Callback; import retrofit2.Response;

public class PlaceViewFragment extends Fragment implements OnMapReadyCallback {

    private double latitude = 0.0;
    private double longitude = 0.0;
    private String placeName = "약속 장소";
    private long appointmentId = -1;

    private GoogleMap mMap;
    private boolean isSharing = false;
    private String myUserId;
    private FusedLocationProviderClient fusedLocationClient;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

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
            requestAndUploadLocation();
        } else {
            uploadLocation(null);
        }
    }

    private void requestAndUploadLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("PERMISSION", "위치 권한 없음");
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                uploadLocation(location);
            }
        }).addOnFailureListener(e -> {
            Log.e("LOCATION", "위치 가져오기 실패", e);
        });
    }

    private void uploadLocation(@Nullable Location location) {
        if (myUserId == null) return;

        double lat = (location != null) ? location.getLatitude() : latitude;
        double lng = (location != null) ? location.getLongitude() : longitude;
        boolean sharingFlag = (location != null);

        ApiService apiService = RetrofitClient.getApiService();
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

                        String nameKey = loc.getName(); // ✅ 이름 기준으로 색상 분기

                        if (!userColorMap.containsKey(nameKey)) {
                            userColorMap.put(nameKey, hues[colorIndex % hues.length]);
                            colorIndex++;
                        }

                        float color = userColorMap.get(nameKey);

                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(loc.getName()) // 이름 표시 유지
                                .icon(BitmapDescriptorFactory.defaultMarker(color));
                        mMap.addMarker(options);

                        if (myUserId != null && myUserId.equals(loc.getUserId())) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        }
                    }

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
