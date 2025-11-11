package org.example.model.hotel;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.user.User;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false, unique = true)
    private User manager;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "receptionists")
    private List<User> receptionists;
}
