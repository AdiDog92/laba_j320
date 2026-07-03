package src.ui_module.model.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import src.ui_module.model.AddressModel;
import src.ui_module.model.ClientModel;
import src.ui_module.model.dto.AddressDto;
import src.ui_module.model.dto.ClientDto;
import src.ui_module.model.ClientTypeEnum;

@Component
@RequiredArgsConstructor
public class ClientDtoToModel implements Function<ClientDto, ClientModel> {

	private final Function<AddressDto, AddressModel> addressDtoToModel;

	@Override
	public ClientModel apply(ClientDto dto) {
		return ClientModel.builder()
		.id(dto.getClientId())
		.clientName(dto.getClientName())
		.type(ClientTypeEnum.getClientType(dto.getType()))
		.added(dto.getAdded())
		.addresses(dto.getAddresses() == null ? List.of() : dto.getAddresses().stream().map(addressDtoToModel).collect(Collectors.toList()))
		.build();
	}
}
