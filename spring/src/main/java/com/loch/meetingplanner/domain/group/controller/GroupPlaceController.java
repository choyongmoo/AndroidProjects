package com.loch.meetingplanner.domain.group.controller;

import com.loch.meetingplanner.domain.group.dto.CreatePlaceRequest;
import com.loch.meetingplanner.domain.group.dto.PlaceResponse;
import com.loch.meetingplanner.domain.group.service.GroupService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/places")
public class GroupPlaceController {

    private final GroupService groupService;

    public GroupPlaceController(GroupService groupService) {
        this.groupService = groupService;
    }

    // 장소 즐겨찾기 등록
    @PostMapping
    public ResponseEntity<Void> addPlaceToGroup(
            @PathVariable Long groupId,
            @RequestBody @Valid CreatePlaceRequest request) {
        groupService.addPlaceToGroup(groupId, request);
        return ResponseEntity.ok().build();
    }

    // 즐겨찾기 목록 조회
    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getGroupPlaces(@PathVariable Long groupId) {
        List<PlaceResponse> places = groupService.getGroupPlaces(groupId);
        return ResponseEntity.ok(places);
    }
}
