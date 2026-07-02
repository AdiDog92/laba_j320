package src.backend_module.service.impl;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import src.backend_module.model.dto.AddressDto;
import src.backend_module.model.dto.ClientDto;
import src.backend_module.model.entity.AddressEntity;
import src.backend_module.model.entity.ClientEntity;
import src.backend_module.repository.ClientRepository;
import src.backend_module.service.ClientService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final Function<ClientEntity, ClientDto> clientEntityToClientDtoFunction;
    private final Function<ClientDto, ClientEntity> clientDtoToClientEntityFunction;
    private final Function<AddressDto, AddressEntity> addressDtoToAddressEntityFunction;

    @Override
    public Stream<ClientDto> findAllClients() {
        return clientRepository.findAll().stream()
                .map(clientEntityToClientDtoFunction);
    }

    @Override
    public Optional<ClientDto> findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .map(clientEntityToClientDtoFunction);
    }

    @Override
    public Optional<ClientDto> createClient(ClientDto clientDto) {

        return Optional.ofNullable(clientDto)
                .map(this::personIdSetNull)
                .map(clientDtoToClientEntityFunction)
                .map(this::initializationPerson)
                .map(clientRepository::save)
                .map(clientEntityToClientDtoFunction);
    }

    @Override
    public Optional<ClientDto> updateClient(Long clientId, ClientDto clientDto) {
        if (clientDto == null) {
            return Optional.empty();
        }
        return clientRepository.findById(clientId)
                .map(existing -> {
                    existing.setClientName(clientDto.getClientName());
                    existing.setType(clientDto.getType());
                    existing.getAddresses().clear();
                    if (clientDto.getAddresses() != null) {
                        clientDto.getAddresses().stream()
                                .map(addressDtoToAddressEntityFunction)
                                .peek(a -> a.setClient(existing))
                                .forEach(existing.getAddresses()::add);
                    }
                    return clientRepository.save(existing);
                })
                .map(clientEntityToClientDtoFunction);
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public Stream<ClientDto> searchClients(
            String clientName,
            String type,
            String address
    ) {
        log.info("Поиск клиентов по имени {}, типу и адресу {}", clientName, type, address);

        clientName = (clientName != null) ? "%" + clientName + "%" : null;
        type = (type != null) ? "%" + type + "%" : null;
        address = (address != null) ? "%" + address + "%" : null;

        return clientRepository.searchByParam(clientName, type, address).stream()
                .map(clientEntityToClientDtoFunction);
    }

    private ClientDto personIdSetNull(ClientDto dto) {
        dto.setClientId(null);
        return dto;
    }

    private ClientEntity initializationPerson(ClientEntity client) {
        client.getAddresses()
                .forEach(address -> address.setClient(client));

        return client;
    }
}
