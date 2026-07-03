package src.ui_module.service;

import java.util.Optional;
import java.util.stream.Stream;

import src.ui_module.model.dto.ClientDto;
public interface ClientService {

	ClientDto getClientById(Long id);

	Stream<ClientDto> getAllClients();

	Optional<ClientDto> createClient(ClientDto clientDto);

	Optional<ClientDto> updateClient(Long clientId, ClientDto clientDto);
	
	void deleteClient(Long clientId);

	Stream<ClientDto> searchClients(
		String clientName, 
		String type, 
		String address);

}
