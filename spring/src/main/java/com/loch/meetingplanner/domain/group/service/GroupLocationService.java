package com.loch.meetingplanner.domain.group.service;
import com.loch.meetingplanner.domain.user.model.LiveLocation;
import com.loch.meetingplanner.domain.user.model.User;
import com.loch.meetingplanner.domain.user.dto.GroupMemberLocationResponse;
import com.loch.meetingplanner.domain.group.repository.GroupMemberRepository;
import com.loch.meetingplanner.domain.user.repository.LiveLocationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupLocationService {

    private final GroupMemberRepository groupMemberRepository;
    private final LiveLocationRepository liveLocationRepository;

    public List<GroupMemberLocationResponse> getGroupMembersLocation(Long groupId) {
        // 1. 그룹 멤버 조회
        List<User> members = groupMemberRepository.findUsersByGroupId(groupId);

        // 2. 각 멤버의 위치 조회
        return members.stream()
                .map(user -> {
                    LiveLocation location = liveLocationRepository.findById(user.getId()).orElse(null);
                    if (location == null) return null;
                    return new GroupMemberLocationResponse(
                        user.getId(),
                        user.getUsername(),
                        location.getLat(),
                        location.getLng()
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}
