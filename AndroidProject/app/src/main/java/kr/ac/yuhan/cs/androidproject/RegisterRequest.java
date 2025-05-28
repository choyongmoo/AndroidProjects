package kr.ac.yuhan.cs.androidproject;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String displayName;

    public RegisterRequest(String username, String password, String email, String displayName){
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
}
