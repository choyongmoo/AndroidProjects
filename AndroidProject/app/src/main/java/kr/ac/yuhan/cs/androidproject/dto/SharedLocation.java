package kr.ac.yuhan.cs.androidproject.dto;

public class SharedLocation {
    private String userId;
    private Long appointmentId;
    private double latitude;
    private double longitude;
    private boolean isSharing;
    // 🔥 추가할 필드
    private String name;



    public SharedLocation(String userId, Long appointmentId, double latitude, double longitude, boolean isSharing) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSharing = isSharing;
    }

    // getter & setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isSharing() { return isSharing; }
    public void setSharing(boolean sharing) { isSharing = sharing; }

    // 🔥 이름 필드용 getter/setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

