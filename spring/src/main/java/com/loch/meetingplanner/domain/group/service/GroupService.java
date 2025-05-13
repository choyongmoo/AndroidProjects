package com.loch.meetingplanner.domain.group.service;
import com.loch.meetingplanner.domain.user.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.group.dto.GroupDetailResponse;
import com.loch.meetingplanner.domain.group.dto.GroupResponse;
import com.loch.meetingplanner.domain.group.model.Group;
import com.loch.meetingplanner.domain.group.model.GroupMember;
import com.loch.meetingplanner.domain.group.repository.GroupMemberRepository;
import com.loch.meetingplanner.domain.group.repository.GroupRepository;
import com.loch.meetingplanner.domain.group.dto.CreateGroupRequest;
import com.loch.meetingplanner.domain.user.model.User;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        GroupMemberRepository groupMemberRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    // 그룹 생성
    public void createGroup(CreateGroupRequest dto, User user) {
        Group group = new Group();
        group.setGroupName(dto.groupname());
        group.setCreatedBy(user);
        groupRepository.save(group);
    }

    // 그룹 멤버 추가
    public void addMember(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("그룹 없음"));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("유저 없음"));

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);

        groupMemberRepository.save(member);
    }

    //그룹 목록 보기
    public List<GroupResponse> getGroupsForUser(User user) {
        List<Group> groups = groupRepository.findByCreatedBy(user);

        return groups.stream()
            .map(g -> new GroupResponse(g.getId(), g.getGroupName()))
            .toList();
    }

    //그룹 상세 목록 보기
    public GroupDetailResponse getGroupDetail(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("그룹 없음"));

        List<User> members = groupMemberRepository.findUsersByGroup(group);
        return new GroupDetailResponse(group.getGroupName(), members);
    }
}
