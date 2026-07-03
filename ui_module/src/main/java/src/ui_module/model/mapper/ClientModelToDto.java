package src.ui_module.model.mapper;

import org.springframework.stereotype.Component;

import src.ui_module.model.AddressModel;
import src.ui_module.model.ClientModel;
import src.ui_module.model.dto.AddressDto;
import src.ui_module.model.dto.ClientDto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClientModelToDto implements Function<ClientModel, ClientDto> {

	private final Function<AddressModel, AddressDto> addressModelToDto;

	@Override
	public ClientDto apply(ClientModel model) {
		return ClientDto.builder()
		.clientId(model.getId())
		.clientName(model.getClientName())
		.type(model.getType().getDescription())
		.added(model.getAdded())
    .addresses(model.getAddresses() == null 
			? List.of() 
			: model.getAddresses()
			.stream()
			.map(addressModelToDto)
			.collect(Collectors.toList()))
		.build();
	}
}
