package src.backend_module.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = "/search")
    public ResponseEntity<List<ClientDto>> searchClients(
            @RequestParam(value = "client_name", required
                    = false) String clientName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String address
    ) {
        log.info("Получен запрос на поиск клиентов по имени {}, типу и адресу {}", clientName, type, address);
        return ResponseEntity.ok(clientService.searchClients(clientName, type, address).toList());
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
            @Valid @RequestBody ClientDto clientDto
    ) {

        log.info("Получен запрос на создание клиента");

        return clientService.createClient(clientDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());

    }

    @PutMapping(value = "/{clientId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable Long clientId,
            @Valid @RequestBody ClientDto clientDto
    ) {
        return clientService.updateClient(clientId, clientDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{clientId}")
    public ResponseEntity<Void> deleteClientById(
            @PathVariable Long clientId
    ) {

        log.info("Получен запрос на удаление клиента с id {}", clientId);

        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }

}
