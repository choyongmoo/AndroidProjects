package kr.ac.yuhan.cs.androidproject.dto;

public class AppointmentResponse {
    private Long id;
    private String groupId;
    private String placeId;
    private String placeName; // ✅ 추가!
    private String time;
    private String creatorId;
    private String createdAt;
    private Integer penalty;
    private String title;

    public AppointmentResponse() {}

    public AppointmentResponse(Long id, String groupId, String placeId, String placeName, String time,
                               String creatorId, String createdAt, Integer penalty, String title) {
        this.id = id;
        this.groupId = groupId;
        this.placeId = placeId;
        this.placeName = placeName; // ✅ 추가!
        this.time = time;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.penalty = penalty;
        this.title = title;
    }

    // --- getter/setter 포함해서 아래 추가 ---
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    // 기존 getter/setter는 그대로 둡니다.
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
