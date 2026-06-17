package src.backend_module.service.impl;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
