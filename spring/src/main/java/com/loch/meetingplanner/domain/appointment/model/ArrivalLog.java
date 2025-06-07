package com.loch.meetingplanner.domain.appointment.model;

import com.loch.meetingplanner.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "arrival_logs", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "appointment_id", "user_id" }, name = "unique_arrival")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_arrival_logs_appointments"))
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_arrival_logs_users"))
    private User user;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "is_late")
    private Boolean isLate;
}
