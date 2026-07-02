package src.ui_module.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {

	private Long id;
	private String ip;
	private String mac;
	private String model;
	private String address;

}
