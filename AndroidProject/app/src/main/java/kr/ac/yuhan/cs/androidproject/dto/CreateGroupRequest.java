package kr.ac.yuhan.cs.androidproject.dto;

import com.google.gson.annotations.SerializedName;

public class CreateGroupRequest {
    @SerializedName("groupname")
    private String groupName;

    public CreateGroupRequest(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
