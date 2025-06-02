package com.loch.meetingplanner.domain.group.service;

import com.loch.meetingplanner.domain.user.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loch.meetingplanner.domain.group.dto.CreateGroupRequest;
import com.loch.meetingplanner.domain.group.dto.CreatePlaceRequest;
import com.loch.meetingplanner.domain.group.dto.GroupDetailResponse;
import com.loch.meetingplanner.domain.group.dto.GroupResponse;
import com.loch.meetingplanner.domain.group.dto.PlaceResponse;
import com.loch.meetingplanner.domain.group.model.Group;
import com.loch.meetingplanner.domain.group.model.GroupMember;
import com.loch.meetingplanner.domain.group.model.GroupPlace;
import com.loch.meetingplanner.domain.group.model.Place;
import com.loch.meetingplanner.domain.group.repository.GroupMemberRepository;
import com.loch.meetingplanner.domain.group.repository.GroupPlaceRepository;
import com.loch.meetingplanner.domain.group.repository.GroupRepository;
import com.loch.meetingplanner.domain.group.repository.PlaceRepository;
import com.loch.meetingplanner.domain.user.model.User;

@Service
public class GroupService {

    private final GroupPlaceRepository groupPlaceRepository;
    private final PlaceRepository placeRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    

    public GroupService(
        GroupRepository groupRepository,
        GroupMemberRepository groupMemberRepository,
        UserRepository userRepository,
        PlaceRepository placeRepository,
        GroupPlaceRepository groupPlaceRepository
    ) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.groupPlaceRepository = groupPlaceRepository;
    }


    // 그룹 생성
    public void createGroup(CreateGroupRequest dto, User user) {
        Group group = new Group();
        group.setGroupName(dto.groupname());
        group.setCreatedBy(user);

        Group savedGroup = groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(savedGroup);
        groupMember.setUser(user);
        groupMemberRepository.save(groupMember);
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

    // 그룹 목록 보기
    public List<GroupResponse> getGroupsForUser(User user) {
        List<Group> groups = groupRepository.findByCreatedBy(user);

        return groups.stream()
                .map(g -> new GroupResponse(g.getId(), g.getGroupName()))
                .toList();
    }

    // 그룹 상세 목록 보기
    public GroupDetailResponse getGroupDetail(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹 없음"));

        List<User> members = groupMemberRepository.findUsersByGroup(group);
        return new GroupDetailResponse(group.getGroupName(), members);
    }

    //장소 즐겨찾기 등록
    public void addPlaceToGroup(Long groupId, CreatePlaceRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        Place place = placeRepository.save(Place.builder()
            .name(request.name())
            .lat(request.lat())
            .lng(request.lng())
            .address(request.address())
            .build());

        GroupPlace groupPlace = GroupPlace.builder()
            .group(group)
            .place(place)
            .build();

        groupPlaceRepository.save(groupPlace);
    }

    //즐겨찾기 장소 목록 조회
    public List<PlaceResponse> getGroupPlaces(Long groupId) {
        return groupPlaceRepository.findByGroupId(groupId).stream()
            .map(gp -> {
                Place p = gp.getPlace();
                return new PlaceResponse(p.getId(), p.getName(), p.getLat(), p.getLng(), p.getAddress());
            })
            .toList();
    }
}
