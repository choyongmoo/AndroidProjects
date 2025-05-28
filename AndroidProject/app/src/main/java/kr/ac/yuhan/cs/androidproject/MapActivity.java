package kr.ac.yuhan.cs.androidproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_ui);

        // 1) Places SDK 초기화
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "키를 입력하세요", Locale.KOREA);
        }
        PlacesClient placesClient = Places.createClient(this);

        // 2) 자동완성 프래그먼트 세팅
        AutocompleteSupportFragment autoFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autoFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autoFragment.setHint("장소 검색");
        autoFragment.setCountry("KR");
        autoFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null && mMap != null) {
                    selectedLatLng = place.getLatLng();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(selectedLatLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                }
            }
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapActivity.this, "검색 오류: " + status.getStatusMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 3) FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 4) 권한 체크 & 지도 초기화
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }

        // 5) 하단 버튼
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnShare).setOnClickListener(v -> {
            if (selectedLatLng == null) {
                Toast.makeText(this, "위치 선택 후 공유하세요", Toast.LENGTH_SHORT).show();
                return;
            }
            // 위경도 → 주소 변환
            String addressStr = "주소를 찾을 수 없습니다";
            try {
                List<Address> list = new Geocoder(this, Locale.KOREA)
                        .getFromLocation(selectedLatLng.latitude,
                                selectedLatLng.longitude, 1);
                if (!list.isEmpty()) {
                    addressStr = list.get(0).getAddressLine(0);
                }
            } catch (IOException ignored){ }

            new AlertDialog.Builder(this)
                    .setTitle("공유 위치")
                    .setMessage(addressStr)
                    .setPositiveButton("공유", (d,w) -> {
                        // 용무형이 구현 예정
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 15));
                }
            });
            mMap.setOnMapClickListener(latLng -> {
                selectedLatLng = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("선택한 위치"));
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE
                && grantResults.length>0
                && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            initMap();
        } else {
            Toast.makeText(this, "위치 권한 필요", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

