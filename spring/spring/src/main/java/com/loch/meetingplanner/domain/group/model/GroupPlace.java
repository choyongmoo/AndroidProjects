package com.loch.meetingplanner.domain.group.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_places", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "group_id", "place_id" }, name = "unique_group_place")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_places_groups"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_places_places"))
    private Place place;
}
