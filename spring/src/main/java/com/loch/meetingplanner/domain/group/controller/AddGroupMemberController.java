package com.loch.meetingplanner.domain.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.group.dto.AddGroupMemberRequest;
import com.loch.meetingplanner.domain.group.service.GroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups")
public class AddGroupMemberController {

    private final GroupService groupService;

    AddGroupMemberController(GroupService groupService) {
        this.groupService = groupService;
    }

    // 그룹 멤버 추가
    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long groupId,
            @RequestBody @Valid AddGroupMemberRequest dto) {
        groupService.addMember(groupId, dto.username());
        return ResponseEntity.ok().build();
    }
}
