package src.backend_module.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import src.backend_module.model.dto.AddressDto;
import src.backend_module.model.dto.ClientDto;
import src.backend_module.model.entity.AddressEntity;
import src.backend_module.model.entity.ClientEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class ClientEntityToDto implements Function<ClientEntity, ClientDto> {

	private final Function<AddressEntity, AddressDto> addressEntityToAddressDtoFunction;

	@Override
	public ClientDto apply(ClientEntity entity) {
		return ClientDto.builder()
						.clientId(entity.getClientId())
						.clientName(entity.getClientName())
						.type(entity.getType())
						.added(entity.getAdded())
						.addresses(
							Optional.ofNullable(entity.getAddresses())
									.orElse(new HashSet<>())
									.stream()
									.map(addressEntityToAddressDtoFunction)
									.toList()
								)
						.build();
	}
}
