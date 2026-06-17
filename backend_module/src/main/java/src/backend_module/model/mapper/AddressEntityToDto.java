package src.backend_module.model.mapper;

import org.springframework.stereotype.Component;
import src.backend_module.model.dto.AddressDto;
import src.backend_module.model.entity.AddressEntity;

import java.util.function.Function;

@Component
public class AddressEntityToDto implements Function<AddressEntity, AddressDto> {

	@Override
	public AddressDto apply(AddressEntity entity) {
		return AddressDto.builder()
						.id(entity.getId())
						.ip(entity.getIp())
						.mac(entity.getMac())
						.model(entity.getModel())
						.address(entity.getAddress())
						.build();
	}
}
