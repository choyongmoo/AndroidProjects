package com.loch.meetingplanner.domain.group.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loch.meetingplanner.domain.group.service.GroupLocationService;
import com.loch.meetingplanner.domain.user.dto.GroupMemberLocationResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupLocationController {
    
    private final GroupLocationService groupLocationService;

    @GetMapping("/{groupId}/locations") //해당 그룹에 속한 모든 멤버의 위치 정보 조회
    public List<GroupMemberLocationResponse> getGroupLocations(@PathVariable Long groupId) {
        return groupLocationService.getGroupMembersLocation(groupId);
    }
}
