package com.loch.meetingplanner.domain.user.dto;

//결과로 주는 것은 사용자 정보이기 때문에 user 도메인에다가 박아둠
public record GroupMemberLocationResponse(Long userId, String username, Double lat, Double lng) {
    
} 
