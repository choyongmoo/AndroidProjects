package kr.ac.yuhan.cs.androidproject.dto;

public class PlaceResponse {
    private Long id;
    private String name;
    private double lat;
    private double lng;
    private String address;

    public Long getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
}
