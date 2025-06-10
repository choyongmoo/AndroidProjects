package kr.ac.yuhan.cs.androidproject.dto;

import java.util.List;

public class GroupDetail {

    private String groupName;
    private List<GroupMember> members;

    public GroupDetail() {}

    public GroupDetail(String groupName, List<GroupMember> members) {
        this.groupName = groupName;
        this.members = members;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {  // setter 추가
        this.groupName = groupName;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {  // setter 추가
        this.members = members;
    }
}
