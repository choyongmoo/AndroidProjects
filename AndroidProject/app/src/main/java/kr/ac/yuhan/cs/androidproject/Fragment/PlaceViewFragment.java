package kr.ac.yuhan.cs.androidproject.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import kr.ac.yuhan.cs.androidproject.ApiService;
import kr.ac.yuhan.cs.androidproject.R;
import kr.ac.yuhan.cs.androidproject.RetrofitClient;
import kr.ac.yuhan.cs.androidproject.TokenManager;
import kr.ac.yuhan.cs.androidproject.dto.SharedLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceViewFragment extends Fragment implements OnMapReadyCallback {

    private double latitude = 0.0;
    private double longitude = 0.0;
    private String placeName = "약속 장소";
    private long appointmentId = -1;

    private GoogleMap mMap;
    private boolean isSharing = false;
    private String myUserId;
    private FusedLocationProviderClient fusedLocationClient;

    private EditText etNameSearch;
    private Map<String, LatLng> userMarkerMap = new HashMap<>();

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

        etNameSearch = view.findViewById(R.id.etNameSearch);
        Button btnFind = view.findViewById(R.id.btnFind);
        Button btnCancelFocus = view.findViewById(R.id.btnCancelFocus);

        btnFind.setOnClickListener(v -> {
            String targetName = etNameSearch.getText().toString().trim();
            if (!targetName.isEmpty() && userMarkerMap.containsKey(targetName)) {
                LatLng targetLatLng = userMarkerMap.get(targetName);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 17));
            }
        });

        btnCancelFocus.setOnClickListener(v -> {
            LatLng mainLoc = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainLoc, 15));
        });
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

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setNumUpdates(1);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("LOCATION", "위치 정보 없음");
                    return;
                }
                Location location = locationResult.getLastLocation();
                uploadLocation(location);
                fusedLocationClient.removeLocationUpdates(this);
            }
        }, null);
    }

    private void uploadLocation(@Nullable Location location) {
        if (myUserId == null) return;

        double lat = (location != null) ? location.getLatitude() : 0.0;
        double lng = (location != null) ? location.getLongitude() : 0.0;
        boolean sharingFlag = (location != null);

        ApiService apiService = RetrofitClient.getApiService();
        SharedLocation loc = new SharedLocation(myUserId, appointmentId, lat, lng, sharingFlag);

        apiService.uploadSharedLocation(loc).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                loadSharedMarkers(location != null);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("SHARE", "위치 업로드 실패", t);
            }
        });
    }

    private void loadSharedMarkers(boolean moveCamera) {
        if (mMap == null) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getSharedLocations(appointmentId).enqueue(new Callback<List<SharedLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<SharedLocation>> call, @NonNull Response<List<SharedLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMap.clear();
                    userMarkerMap.clear();

                    float[] hues = {
                            BitmapDescriptorFactory.HUE_GREEN,
                            BitmapDescriptorFactory.HUE_BLUE,
                            BitmapDescriptorFactory.HUE_ORANGE,
                            BitmapDescriptorFactory.HUE_VIOLET,
                            BitmapDescriptorFactory.HUE_CYAN
                    };

                    Map<String, Float> userColorMap = new HashMap<>();
                    int colorIndex = 0;
                    LatLng myLocationLatLng = null;

                    for (SharedLocation loc : response.body()) {
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                        String nameKey = loc.getName();

                        if (!userColorMap.containsKey(nameKey)) {
                            userColorMap.put(nameKey, hues[colorIndex % hues.length]);
                            colorIndex++;
                        }

                        float color = userColorMap.get(nameKey);
                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(nameKey)
                                .icon(BitmapDescriptorFactory.defaultMarker(color));
                        mMap.addMarker(options);
                        userMarkerMap.put(nameKey, latLng);

                        if (myUserId != null && myUserId.equals(loc.getUserId())) {
                            myLocationLatLng = latLng;
                        }
                    }

                    // 약속 장소 마커 추가
                    LatLng mainLoc = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(mainLoc)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    if (moveCamera) {
                        if (myLocationLatLng != null) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 17));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainLoc, 15));
                        }
                    }
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
        loadSharedMarkers(false);
    }
}
