package src.ui_module.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import src.ui_module.ValidationException;
import src.ui_module.model.AddressModel;
import src.ui_module.model.ClientModel;
import src.ui_module.model.ClientTypeEnum;
import src.ui_module.model.dto.ClientDto;
import src.ui_module.service.ClientService;

@Slf4j
@Controller
@AllArgsConstructor
public class ClientController {

	private final ClientService clientService;

	private final Function<ClientDto, ClientModel> clientDtoToModel;
	private final Function<ClientModel, ClientDto> clientModelToDto;

	@GetMapping(value = {"/", "/clients"})
	public String getAllClientsPage(Model model) {

		log.info("Открытие страницы с пользователями");

		List<ClientModel> clients = clientService.getAllClients()
		.map(clientDtoToModel)
		.collect(Collectors.toList());

		model.addAttribute("clients", clients);
		
		log.info("Страница с пользователями успешно открыта");

		return "index";

	}

	@GetMapping(value = "/clients/create")
	public String createClientPage(Model model) {
		log.info("Открытие страницы с созданием пользователя");
		model.addAttribute("client", new ClientModel());
		return "create_client_page";
	}

	@PostMapping(value = "/clients/create")
	public String createClient(Model model, 
		@RequestParam String clientName,
		@RequestParam String type,
		@RequestParam String ip,
		@RequestParam String mac,
		@RequestParam String modelName,
		@RequestParam String address
	) {
		log.info("Получен запрос на создание пользователя: {}", clientName);

		ClientModel clientModel = ClientModel.builder()
		.clientName(clientName)
		.type(ClientTypeEnum.getClientType(type))
		.addresses(List.of(AddressModel.builder()
		.ip(ip)
		.mac(mac)
		.model(modelName)
		.address(address)
		.build()))
		.build();

		log.info("Модель пользователя успешно создана: {}", clientModel);

		try {
			ClientDto createdClient = clientModelToDto.apply(clientModel);

			log.info("Модель пользователя успешно преобразована в DTO: {}", createdClient);
	
			clientService.createClient(createdClient);

			log.info("Пользователь успешно создан: {}", createdClient);

			return "redirect:/clients";
		} catch (ValidationException e) {

			log.error("Ошибка при создании пользователя: {}", e.getMessage());

			model.addAttribute("error", e.getMessage());

			return "error_page";

		} catch (Exception e) {
			log.error("непредвиденная ошибка при создании пользователя: {}", e.getMessage());

			throw e;
		}
	}
}
