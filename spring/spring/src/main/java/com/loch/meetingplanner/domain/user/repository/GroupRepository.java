package com.loch.meetingplanner.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.loch.meetingplanner.domain.user.model.Group;
import com.loch.meetingplanner.domain.user.model.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    //그룹 이름으로 찾기
    Optional<Group> findByGroupName(String groupName);  

    //이 유저가 만든 그룹을 보여줌
    List<Group> findByCreatedBy(User user);

} 
