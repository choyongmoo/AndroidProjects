package kr.ac.yuhan.cs.androidproject.dto;

public class NewPasswordRequest {
    private String username;
    private String email;
    private String newPassword;

    public NewPasswordRequest(String username, String email, String newPassword) {
        this.username = username;
        this.email = email;
        this.newPassword = newPassword;
    }
}
