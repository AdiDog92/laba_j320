package src.backend_module.service;

import java.util.Optional;
import java.util.stream.Stream;

import src.backend_module.model.dto.ClientDto;

public interface ClientService {

    Stream<ClientDto> findAllClients();

    Optional<ClientDto> findClientById(Long clientId);

    Optional<ClientDto> createClient(ClientDto clientDto);

    Optional<ClientDto> updateClient(Long clientId, ClientDto clientDto);

    void deleteClient(Long clientId);

    Stream<ClientDto> searchClients(
            String clientName,
            String type,
            String address
    );

}
