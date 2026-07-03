package src.ui_module.model.mapper;

import org.springframework.stereotype.Component;

import java.util.function.Function;

import src.ui_module.model.AddressModel;
import src.ui_module.model.dto.AddressDto;

@Component
public class AddressDtoToModel implements Function<AddressDto, AddressModel> {

	@Override
	public AddressModel apply(AddressDto dto) {
		return AddressModel.builder()
		.id(dto.getId())
		.ip(dto.getIp())
		.mac(dto.getMac())
		.model(dto.getModel())
		.address(dto.getAddress())
		.build();
	}
}
