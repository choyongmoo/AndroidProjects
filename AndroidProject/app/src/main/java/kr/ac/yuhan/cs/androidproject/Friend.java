package kr.ac.yuhan.cs.androidproject;

public class Friend {
    private String displayName;
    private String profileUrl;

    public Friend(String displayName, String profileUrl){
        this.displayName = displayName;
        this.profileUrl = profileUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

}
