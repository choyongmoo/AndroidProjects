package kr.ac.yuhan.cs.androidproject;

// 약속 정보를 담는 데이터 클래스
public class Meeting {
    public String title;     // 약속 제목
    public String dateTime;  // 약속 날짜 + 시간
    public String location;  // 약속 장소

    public Meeting(String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
    }
}
