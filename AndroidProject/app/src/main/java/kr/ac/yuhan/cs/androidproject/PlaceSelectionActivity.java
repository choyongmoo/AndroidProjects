package kr.ac.yuhan.cs.androidproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PlaceSelectionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker;
    private LatLng selectedLatLng;
    private String selectedAddress;
    private String selectedPlaceName;
    private String selectedPlaceId; // <- 추가
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.483034, 126.782759);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_select_location);

        // Places API 초기화
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDnQRzK6J7hXF5k-ZFukiHyA-L2YJ0J_b0");
        }

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 검색창 연결
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    selectedLatLng = place.getLatLng();
                    selectedAddress = place.getAddress();
                    selectedPlaceName = place.getName();
                    selectedPlaceId = place.getId(); // <- 여기 추가!
                    moveMarker(selectedLatLng);
                }

                @Override
                public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                    Toast.makeText(PlaceSelectionActivity.this, "검색 오류: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 버튼
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnSelect.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLatLng.latitude);
                resultIntent.putExtra("longitude", selectedLatLng.longitude);
                resultIntent.putExtra("address", selectedAddress != null ? selectedAddress : "주소 없음");
                resultIntent.putExtra("name", selectedPlaceName != null ? selectedPlaceName : selectedAddress);
                resultIntent.putExtra("placeId", selectedPlaceId != null ? selectedPlaceId : ""); // ← 이 줄 추가
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "장소를 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        moveMarker(DEFAULT_LOCATION);

        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            selectedAddress = getAddressFromLatLng(latLng);
            selectedPlaceName = selectedAddress; // 주소를 장소 이름으로
            moveMarker(latLng);
        });
    }

    private void moveMarker(LatLng latLng) {
        if (mMap != null) {
            if (currentMarker != null) currentMarker.remove();
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("선택한 위치"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "알 수 없는 위치";
    }
}


