// 개인 위치 공유 기능을 위해 생성할 파일:
// 공유 위치 데이터의 CRUD가 필요

// 1. domain/location/model/SharedLocation.java
package com.loch.meetingplanner.domain.location.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // 어느 사이용자가 공유 했는지
    private Long appointmentId;
    private double latitude;
    private double longitude;
    private boolean isSharing; // 공유 상태

    private LocalDateTime updatedAt; // 최신 위치 수정 시간
}
