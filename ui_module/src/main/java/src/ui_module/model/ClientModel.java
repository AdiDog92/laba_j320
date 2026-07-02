package src.ui_module.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {

	private Long id;
	private String clientName;
	private ClientTypeEnum type;
	private LocalDate added;
	private List<AddressModel> addresses;

}
