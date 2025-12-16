package org.example.validators;


import org.example.DTO.ReservationCreationDTO;
import org.example.exceptions.ValidationException;
import org.example.model.room.Room;

public class ReservationValidator {

    public static void validate(ReservationCreationDTO dto) {
        int totalCapacity = dto.getSelectedRooms().stream().mapToInt(Room::getCapacity).sum();

        if (dto.getSelectedRooms().isEmpty()) {
            throw new ValidationException("Please select at least one room");
        }
        if (dto.getSelectedClients().isEmpty()) {
            throw new ValidationException("Please select at least one client");
        }
        if (dto.getReservationType() == null) {
            throw new ValidationException("Please select at least one reservation type");
        }
        if (dto.getSelectedClients().size() > totalCapacity) {
            throw new ValidationException("Clients are more than the capacity of the rooms");
        }


    }
}
