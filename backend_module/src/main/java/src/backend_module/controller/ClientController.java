package src.backend_module.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import src.backend_module.model.dto.ClientDto;
import src.backend_module.service.ClientService;

@Slf4j
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping()
    public ResponseEntity<List<ClientDto>> getAllClients() {

				log.info("Получен запрос всех клиентов");

        return ResponseEntity.ok(clientService.findAllClients().toList());
    }

		@GetMapping("/{clientId}")
		public ResponseEntity<ClientDto> getClientById(
						@PathVariable Long clientId
			) {

				log.info("Получен запрос c id клиентa {} ", clientId);

				return clientService.findClientById(clientId)
								.map(ResponseEntity::ok)
								.orElse(ResponseEntity.notFound().build());
			}

			@PostMapping(consumes = "application/json", produces = "application/json")
			public ResponseEntity<ClientDto> createClient(
					@RequestBody ClientDto clientDto
			) {

				log.info("Получен запрос на создание клиента");

				return clientService.createClient(clientDto)
								.map(ResponseEntity::ok)
								.orElse(ResponseEntity.notFound().build());

			}
}
