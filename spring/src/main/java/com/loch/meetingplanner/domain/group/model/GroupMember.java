package com.loch.meetingplanner.domain.group.model;

import com.loch.meetingplanner.domain.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "group_members")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_members_seq_gen")
    @SequenceGenerator(name = "group_members_seq_gen", sequenceName = "group_members_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;
}
