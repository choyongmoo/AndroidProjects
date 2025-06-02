package com.loch.meetingplanner.domain.user.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "live_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveLocation {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_live_locations_users"))
    private User user;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }
}
