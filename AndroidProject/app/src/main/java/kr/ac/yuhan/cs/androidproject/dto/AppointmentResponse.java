package kr.ac.yuhan.cs.androidproject.dto;

public class AppointmentResponse {
    private Long id;
    private String groupId;
    private String placeId;
    private String time;
    private String creatorId;
    private String createdAt;
    private Integer penalty;
    private String title;

    public AppointmentResponse() {}

    public AppointmentResponse(Long id, String groupId, String placeId, String time, String creatorId, String createdAt, Integer penalty, String title) {
        this.id = id;
        this.groupId = groupId;
        this.placeId = placeId;
        this.time = time;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.penalty = penalty;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
