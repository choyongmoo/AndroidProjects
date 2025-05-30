package kr.ac.yuhan.cs.androidproject.dto;

public class Meeting {
    private String title;
    private String time;
    private String location;

    public Meeting(String title, String time, String location) {
        this.title = title;
        this.time = time;
        this.location = location;
    }

    public String getTitle() { return title; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
}
