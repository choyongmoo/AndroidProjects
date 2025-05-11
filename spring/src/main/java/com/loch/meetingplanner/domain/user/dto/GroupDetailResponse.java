package com.loch.meetingplanner.domain.user.dto;

import java.util.List;

import com.loch.meetingplanner.domain.user.model.User;

public record GroupDetailResponse(String groupName, List<User> members) {} 


