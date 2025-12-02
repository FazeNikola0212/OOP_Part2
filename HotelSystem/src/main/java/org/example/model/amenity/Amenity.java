package org.example.model.amenity;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeasonAmenity season;

    @ManyToMany(mappedBy = "amenities")
    private List<Hotel> hotels = new ArrayList<>();

    private int usageCount;

    private boolean enabled;
}
