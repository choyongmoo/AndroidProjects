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

    @ManyToOne(fetch = FetchType.EAGER)  //fetch = FetchType.LAZY 이거에서 변경(용무)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_created_by_user"))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id", nullable = false, foreignKey = @ForeignKey(name = "fk_created_by_friend"))
    private User friend;
    //fetch = FetchType.LAZY면, friend 필드는 실제 사용할 때까지 DB에서 로드 안 합니다.
    //그래서 friend.getDisplayName() 등 호출해도 값이 안 채워져서 null이 뜹니다.
    //EAGER로 하면 Friend 객체 조회 시 friend User 객체도 즉시 같이 로드돼서 필드 값이 제대로 전달됩니다.

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
