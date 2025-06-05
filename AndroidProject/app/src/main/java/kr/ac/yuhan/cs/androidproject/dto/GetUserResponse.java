package kr.ac.yuhan.cs.androidproject.dto;

public class GetUserResponse {
    private String username;
    private String displayName;
    private String email;
    private String profileImageUrl;
    private String createdAt;
    private String updatedAt;

    public GetUserResponse(String username, String displayName, String email, String profileImageUrl,
                           String createdAt, String updatedAt) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public void setUsername(String username) { this.username = username; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setEmail(String email) { this.email = email; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
