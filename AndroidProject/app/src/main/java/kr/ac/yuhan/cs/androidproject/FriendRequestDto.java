package kr.ac.yuhan.cs.androidproject;

public class FriendRequestDto {
    private String targetUsername;

    public FriendRequestDto(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }
}
