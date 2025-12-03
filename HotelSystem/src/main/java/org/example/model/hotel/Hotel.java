package org.example.model.hotel;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"clients", "amenities", "owner", "manager"})
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    private String address;

    private String city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id",  nullable = false)
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false, unique = true)
    private User manager;

    @OneToMany(mappedBy = "hotel")
    private List<Client> clients  = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "hotel_amenities", joinColumns = @JoinColumn(name ="hotel_id"),
                            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities = new HashSet<>();


}
