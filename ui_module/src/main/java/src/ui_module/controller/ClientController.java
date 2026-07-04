package src.ui_module.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import src.ui_module.ValidationException;
import src.ui_module.model.AddressModel;
import src.ui_module.model.ClientModel;
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

	@GetMapping(value = "/clients/search")
	public String searchClientsPage(Model model,
			@RequestParam(required = false) String clientName,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String address) {

		List<ClientModel> clients = clientService.searchClients(clientName, type, address)
				.map(clientDtoToModel)
				.collect(Collectors.toList());

		model.addAttribute("clients", clients);
		model.addAttribute("clientName", clientName);
		model.addAttribute("type", type);
		model.addAttribute("address", address);

		log.info("Страница с поиском пользователей успешно открыта");
		return "index";
	}

	@GetMapping(value = "/clients/create")
	public String createClientPage(Model model) {
		log.info("Открытие страницы с созданием пользователя");
		model.addAttribute("client", newClientForm());
		return "form_page";
	}

	@PostMapping(value = "/clients/create")
	public String createClient(Model model, @ModelAttribute("client") ClientModel clientModel) {
		log.info("Получен запрос на создание пользователя: {}", clientModel.getClientName());

		try {
			ClientDto createdClient = clientModelToDto.apply(clientModel);
			clientService.createClient(createdClient);
			log.info("Пользователь успешно создан: {}", createdClient.getClientName());
			return "redirect:/clients";
		} catch (ValidationException e) {
			log.error("Ошибка при создании пользователя: {}", e.getMessage());
			model.addAttribute("client", clientModel);
			model.addAttribute("error", e.getMessage());
			model.addAttribute("validationErrors", e.getErrorMessages());
			return "form_page";
		}
	}

	@GetMapping(value = "/clients/edit/{id}")
	public String editClientPage(Model model, @PathVariable Long id) {
		ClientModel clientModel = clientDtoToModel.apply(clientService.getClientById(id));
		ensureAtLeastOneAddress(clientModel);
		model.addAttribute("client", clientModel);
		return "form_page";
	}

	@PostMapping(value = "/clients/edit/{id}")
	public String editClient(Model model,
			@PathVariable Long id,
			@ModelAttribute("client") ClientModel clientModel) {

		clientModel.setId(id);
		log.info("Получен запрос на обновление пользователя: {}", id);

		try {
			ClientDto clientDto = clientModelToDto.apply(clientModel);
			clientService.updateClient(id, clientDto);
			log.info("Пользователь успешно обновлён: {}", id);
			return "redirect:/clients";
		} catch (ValidationException e) {
			log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
			model.addAttribute("client", clientModel);
			model.addAttribute("error", e.getMessage());
			model.addAttribute("validationErrors", e.getErrorMessages());
			return "form_page";
		}
	}

	@PostMapping(value = "/clients/add-address")
	public String addAddress(@ModelAttribute("client") ClientModel clientModel, Model model) {
		if (clientModel.getAddresses() == null) {
			clientModel.setAddresses(new ArrayList<>());
		}
		clientModel.getAddresses().add(new AddressModel());
		model.addAttribute("client", clientModel);
		return "form_page";
	}

	@PostMapping(value = "/clients/remove-address")
	public String removeAddress(@ModelAttribute("client") ClientModel clientModel,
			@RequestParam int addressIndex,
			Model model) {

		if (clientModel.getAddresses() != null && clientModel.getAddresses().size() > 1) {
			clientModel.getAddresses().remove(addressIndex);
		} else {
			model.addAttribute("error", "Должен остаться хотя бы один адрес");
		}
		model.addAttribute("client", clientModel);
		return "form_page";
	}

	@PostMapping(value = "/clients/delete/{id}")
	public String deleteClient(@PathVariable Long id) {
		log.info("Получен запрос на удаление пользователя: {}", id);
		clientService.deleteClient(id);
		return "redirect:/clients";
	}

	private ClientModel newClientForm() {
		ClientModel client = new ClientModel();
		client.setAddresses(new ArrayList<>(List.of(new AddressModel())));
		return client;
	}

	private void ensureAtLeastOneAddress(ClientModel client) {
		if (client.getAddresses() == null || client.getAddresses().isEmpty()) {
			client.setAddresses(new ArrayList<>(List.of(new AddressModel())));
		} else if (!(client.getAddresses() instanceof ArrayList)) {
			client.setAddresses(new ArrayList<>(client.getAddresses()));
		}
	}
}
