package com.loch.meetingplanner.domain.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.loch.meetingplanner.domain.group.model.Group;
import com.loch.meetingplanner.domain.group.model.GroupMember;
import com.loch.meetingplanner.domain.user.model.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // 이 유저가 만든 그룹안에 사람들을 보여줌
    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group = :group")
    List<User> findUsersByGroup(@Param("group") Group group);

    boolean existsByGroupAndUser(Group group, User user);

    //특정 그룹에 속한 사용자들 가져오는 기능 담당
    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") Long groupId);
}
