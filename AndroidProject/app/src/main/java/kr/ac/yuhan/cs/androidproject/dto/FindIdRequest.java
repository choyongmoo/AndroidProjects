package kr.ac.yuhan.cs.androidproject.dto;

public class FindIdRequest {
    private String email;

    public FindIdRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}