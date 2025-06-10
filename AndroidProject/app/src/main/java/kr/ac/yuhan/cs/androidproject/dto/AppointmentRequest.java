package kr.ac.yuhan.cs.androidproject.dto;

public class AppointmentRequest {
    private String title;
    private Integer penalty;
    private long groupId;
    private long placeId;
    private String time;


    public AppointmentRequest() {}

    public AppointmentRequest(String title, Integer penalty, long groupId, long placeId, String time) {
        this.title = title;
        this.penalty = penalty;
        this.groupId = groupId;
        this.placeId = placeId;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
