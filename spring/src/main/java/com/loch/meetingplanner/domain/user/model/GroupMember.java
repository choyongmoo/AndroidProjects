package com.loch.meetingplanner.domain.user.model;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_members_seq")
    @SequenceGenerator(name = "group_members_seq", sequenceName = "group_members_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private User user;
}
