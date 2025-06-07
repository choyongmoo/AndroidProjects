package com.loch.meetingplanner.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "friend_id" }, name = "unique_friend_pair")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_created_by_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false, foreignKey = @ForeignKey(name = "fk_created_by_friend"))
    private User friend;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (status == null) {
            this.status = FriendStatus.PENDING;
        }
    }
}
