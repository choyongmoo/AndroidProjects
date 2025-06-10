package kr.ac.yuhan.cs.androidproject.dto;

public class UpdateUserRequest {
    private String displayName;
    private String email;
    private String password;
    private String profileImageUrl;

    public UpdateUserRequest(){}
    public UpdateUserRequest(String email, String password, String displayName, String profileImageUrl) {
        this.email = email;
        this.password= password;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getDisplayName() { return displayName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getProfileImageUrl() { return profileImageUrl; }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl)
    {
        this.profileImageUrl = profileImageUrl;
    }

}
