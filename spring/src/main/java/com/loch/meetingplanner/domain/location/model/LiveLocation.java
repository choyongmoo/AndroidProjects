package com.loch.meetingplanner.domain.location.model;

import com.loch.meetingplanner.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "live_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_live_locations_users"))
    private User user;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
