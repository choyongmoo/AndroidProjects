package kr.ac.yuhan.cs.androidproject.dto;

public class FriendAcceptDto {
    private String requesterUsername;

    public FriendAcceptDto(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }
}
