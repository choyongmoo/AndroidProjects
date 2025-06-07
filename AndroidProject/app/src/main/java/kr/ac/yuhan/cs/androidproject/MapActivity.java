package kr.ac.yuhan.cs.androidproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    // 유한대학교 기본 좌표
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.483034, 126.782759);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_ui);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 위치 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }

        // 뒤로가기 버튼
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                LatLng target = (location != null)
                        ? new LatLng(location.getLatitude(), location.getLongitude())
                        : DEFAULT_LOCATION;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 15));
                mMap.addMarker(new MarkerOptions().position(target).title("현재 위치"));
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "위치 요청 실패", Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15));
                mMap.addMarker(new MarkerOptions().position(DEFAULT_LOCATION).title("유한대학교"));
            });

        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15));
            mMap.addMarker(new MarkerOptions().position(DEFAULT_LOCATION).title("유한대학교"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMap();
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

