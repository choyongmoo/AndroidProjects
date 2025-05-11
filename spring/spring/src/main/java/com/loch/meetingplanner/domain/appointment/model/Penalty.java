package com.loch.meetingplanner.domain.appointment.model;

import com.loch.meetingplanner.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "penalties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, foreignKey = @ForeignKey(name = "fk_penalties_appointments"))
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_penalties_users"))
    private User payer;

    @Column(nullable = false)
    private Integer amount;
}
