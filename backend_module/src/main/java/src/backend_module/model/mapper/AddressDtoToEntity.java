package src.backend_module.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import src.backend_module.model.dto.AddressDto;
import src.backend_module.model.entity.AddressEntity;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AddressDtoToEntity implements Function<AddressDto, AddressEntity> {

	@Override
	public AddressEntity apply(AddressDto dto) {
			return AddressEntity.builder()
							.id(dto.getId())
							.ip(dto.getIp())
							.mac(dto.getMac())
							.model(dto.getModel())
							.address(dto.getAddress())
							.build();
	}
}
