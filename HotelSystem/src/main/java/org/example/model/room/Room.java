package org.example.model.room;

import jakarta.persistence.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.*;
import org.example.model.hotel.Hotel;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomCategory roomCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus roomStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    @Column(nullable = false)
    private double rating;

    @Transient
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    @Transient
    public boolean isSelected() {
        return selected.get();
    }
    @Transient
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

}
