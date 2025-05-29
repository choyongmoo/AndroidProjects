package kr.ac.yuhan.cs.androidproject;

public class GetUserResponse {
    private String username;
    private String displayName;
    private String email;
    private String profile_image_url;

    public GetUserResponse() { }

    public GetUserResponse(String username, String displayName, String email, String profile_image_url){
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.profile_image_url = profile_image_url;
    }

    @Override
    public String toString() {
        return "GetUserResponse{" +
                "username='" + username + '\'' +
                ", display_name='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profile_image_url + '\'' +
                '}';
    }


    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getProfileImageUrl() { return profile_image_url; }

}
