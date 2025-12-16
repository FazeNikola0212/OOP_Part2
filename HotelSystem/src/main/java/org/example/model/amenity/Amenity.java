package org.example.model.amenity;

import jakarta.persistence.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import org.example.model.hotel.Hotel;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.EAGER)
    private Set<Hotel> hotels = new HashSet<>();

    private int usageCount;

    private boolean enabled;

    @Transient
    public BooleanProperty selected = new SimpleBooleanProperty(false);

    @Transient
    public Boolean isSelected() {
        return selected.get();
    }
    @Transient
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
