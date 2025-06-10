package kr.ac.yuhan.cs.androidproject.dto;

public class Meeting {
    private long id;
    private String title;
    private String time;
    private String location;
    private String groupId;
    private int penalty;
    private long placeId;

    private double latitude;
    private double longitude;

    // id도 포함해서 생성자 수정
    public Meeting(long id, String title, String time, String location,
                   String groupId, int penalty, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.location = location;
        this.groupId = groupId;
        this.penalty = penalty;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getPenalty() {
        return penalty;
    }

    public long getPlaceId() {           // 👉 placeId getter 추가
        return placeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
