package kr.ac.yuhan.cs.androidproject.dto;

public class AddGroupMemberRequest {
    private String username;

    public AddGroupMemberRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
