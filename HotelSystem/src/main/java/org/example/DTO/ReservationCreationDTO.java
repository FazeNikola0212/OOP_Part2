package org.example.DTO;

import lombok.*;
import org.example.model.amenity.Amenity;
import org.example.model.client.Client;
import org.example.model.reservation.ReservationType;
import org.example.model.room.Room;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCreationDTO {
    private String reservationNumber;
    private List<Client> selectedClients;
    private List<Room> selectedRooms;
    private List<Amenity>  selectedAmenities;
    private ReservationType reservationType;
    private LocalDate startDate;
    private LocalDate endDate;

}
