package src.ui_module.model.mapper;

import org.springframework.stereotype.Component;

import java.util.function.Function;

import src.ui_module.model.AddressModel;
import src.ui_module.model.dto.AddressDto;

@Component
public class AddressModelToDto implements Function<AddressModel, AddressDto> {

	@Override
	public AddressDto apply(AddressModel model) {
		return AddressDto.builder()
		.id(model.getId())
		.ip(model.getIp())
		.mac(model.getMac())
		.model(model.getModel())
		.address(model.getAddress())
		.build();
	}
	
}
