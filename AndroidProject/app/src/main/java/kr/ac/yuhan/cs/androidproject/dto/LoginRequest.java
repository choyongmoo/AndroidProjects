package kr.ac.yuhan.cs.androidproject.dto;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
