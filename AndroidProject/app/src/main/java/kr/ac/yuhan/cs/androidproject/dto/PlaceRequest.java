package kr.ac.yuhan.cs.androidproject.dto;

public class PlaceRequest {
    private String name;
    private double lat;
    private double lng;
    private String address;

    public PlaceRequest(String name, double lat, double lng, String address) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    // getter/setter 생략 가능 (필요시 추가)
}