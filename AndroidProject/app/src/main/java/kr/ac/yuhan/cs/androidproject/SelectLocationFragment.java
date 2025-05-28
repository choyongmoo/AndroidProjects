package kr.ac.yuhan.cs.androidproject;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class SelectLocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker selectedMarker;
    private String selectedAddress = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_location, container, false);

        // Places 초기화
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "api 키를 넣으세요", Locale.KOREA);
        }

        // 지도 로딩
        SupportMapFragment mapFrag = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) mapFrag.getMapAsync(this);

        // 자동완성 프래그먼트 설정
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getChildFragmentManager()
                        .findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                        if (selectedMarker != null) selectedMarker.remove();
                        selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                        selectedAddress = place.getAddress();
                    }
                }

                @Override
                public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                    // 오류 로그
                }
            });
        }

        // 선택 버튼
        view.findViewById(R.id.btnSelect).setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("bundleKey_address", selectedAddress);
            getParentFragmentManager().setFragmentResult("requestKey_select", result);
            getParentFragmentManager().popBackStack();
        });

        // 취소 버튼
        view.findViewById(R.id.btnCancel).setOnClickListener(v ->
                getParentFragmentManager().popBackStack());

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLoc = new LatLng(37.4488, 126.6577); // 유한대
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15f));

        mMap.setOnMapClickListener(latLng -> {
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("선택된 위치"));
            selectedAddress = fetchAddress(latLng);
        });
    }

    private String fetchAddress(LatLng latLng) {
        Geocoder geo = new Geocoder(requireContext(), Locale.KOREA);
        try {
            List<Address> list = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (list != null && !list.isEmpty()) {
                return list.get(0).getAddressLine(0);
            }
        } catch (IOException ignored) {}
        return "주소 확인 불가";
    }
}

