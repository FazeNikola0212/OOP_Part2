package org.example.repository.client;

import org.example.model.client.Client;
import org.example.repository.baserepository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findByPhoneNumber(String phoneNumber);
    Client findByEmail(String email);
}
