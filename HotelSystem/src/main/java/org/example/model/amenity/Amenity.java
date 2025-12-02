package org.example.model.amenity;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.hotel.Hotel;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    private int usageCount;

    private boolean enabled;
}
