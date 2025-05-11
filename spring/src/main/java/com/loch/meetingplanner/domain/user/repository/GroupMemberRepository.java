package com.loch.meetingplanner.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.loch.meetingplanner.domain.user.model.Group;
import com.loch.meetingplanner.domain.user.model.GroupMember;
import com.loch.meetingplanner.domain.user.model.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>{

    //이 유저가 만든 그룹안에 사람들을 보여줌
    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group = :group")
    List<User> findUsersByGroup(@Param("group") Group group); 

} 
    
     

