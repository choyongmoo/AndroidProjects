package kr.ac.yuhan.cs.androidproject;

public class LoginResponse {
    private String username;
    private String token;

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return token != null && !token.isEmpty();
    }
}
