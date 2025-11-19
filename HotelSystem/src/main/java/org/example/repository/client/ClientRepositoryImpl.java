package org.example.repository.client;

import org.example.model.client.Client;
import org.example.repository.baserepository.GenericRepositoryImpl;

public class ClientRepositoryImpl extends GenericRepositoryImpl<Client, Long> {
    public ClientRepositoryImpl() {
        super(Client.class);
    }

}
