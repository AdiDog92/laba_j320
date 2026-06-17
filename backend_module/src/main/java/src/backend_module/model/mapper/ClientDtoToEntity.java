package src.backend_module.model.mapper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import src.backend_module.model.dto.AddressDto;
import src.backend_module.model.dto.ClientDto;
import src.backend_module.model.entity.AddressEntity;
import src.backend_module.model.entity.ClientEntity;

@Component
@RequiredArgsConstructor
public class ClientDtoToEntity implements Function<ClientDto, ClientEntity> {

    private final Function<AddressDto, AddressEntity> addressDtoToAddressEntity;

    @Override
    public ClientEntity apply(ClientDto dto) {
        return ClientEntity.builder()
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .type(dto.getType())
                .added(dto.getAdded())
                .addresses(
                        Optional.ofNullable(dto.getAddresses())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(addressDtoToAddressEntity)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
