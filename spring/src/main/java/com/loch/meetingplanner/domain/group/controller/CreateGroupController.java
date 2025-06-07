package com.loch.meetingplanner.domain.group.controller;

import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.config.security.SecurityUserDetails;
import com.loch.meetingplanner.domain.group.dto.CreateGroupRequest;
import com.loch.meetingplanner.domain.group.dto.GroupDetailResponse;
import com.loch.meetingplanner.domain.group.dto.GroupResponse;
import com.loch.meetingplanner.domain.group.service.GroupService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/groups")
public class CreateGroupController {

    private final GroupService groupService;

    public CreateGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // 그룹 생성
    @PostMapping
    public ResponseEntity<Void> createGroup(@RequestBody @Valid CreateGroupRequest dto,
            @AuthenticationPrincipal SecurityUserDetails user) {
        groupService.createGroup(dto, user.getUser());
        return ResponseEntity.ok().build();

    }

    // 그룹 상세
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponse> getGroupDetail(@PathVariable Long groupId) {
        GroupDetailResponse response = groupService.getGroupDetail(groupId);
        return ResponseEntity.ok(response);
    }

    // 그룹 목록
    @GetMapping
    public ResponseEntity<List<GroupResponse>> getGroups(@AuthenticationPrincipal SecurityUserDetails user) {
        List<GroupResponse> groups = groupService.getGroupsForUser(user.getUser());
        return ResponseEntity.ok(groups);
    }
}
