package com.loch.meetingplanner.domain.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loch.meetingplanner.domain.group.dto.PlaceRequest;
import com.loch.meetingplanner.domain.group.dto.PlaceResponse;
import com.loch.meetingplanner.domain.group.model.Place;
import com.loch.meetingplanner.domain.group.repository.PlaceRepository;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @PostMapping
    public ResponseEntity<PlaceResponse> createOrGetPlace(@RequestBody PlaceRequest request) {
        // 위도/경도/주소/이름 동일한 장소 있는지 확인
        Place place = placeRepository
                .findByNameAndLatAndLngAndAddress(
                        request.name(), request.lat(), request.lng(), request.address()
                )
                .orElseGet(() -> {
                    Place newPlace = new Place();
                    newPlace.setName(request.name());
                    newPlace.setLat(request.lat());
                    newPlace.setLng(request.lng());
                    newPlace.setAddress(request.address());
                    return placeRepository.save(newPlace);
                });

        return ResponseEntity.ok(PlaceResponse.from(place));
    }

    // ✅ 장소 ID로 장소 정보 조회하는 GET API 추가
   @GetMapping("/{id}")
public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable Long id) {
    return placeRepository.findById(id)
            .map(place -> ResponseEntity.ok(PlaceResponse.from(place)))
            .orElse(ResponseEntity.notFound().build()); // ❗타입 일치하게 수정
    }
}
