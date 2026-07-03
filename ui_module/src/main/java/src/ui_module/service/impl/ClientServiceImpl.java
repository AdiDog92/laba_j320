package src.ui_module.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import src.ui_module.ValidationErrorResponse;
import src.ui_module.ValidationException;
import src.ui_module.model.dto.ClientDto;
import src.ui_module.rest_client.ExternalApiException;
import src.ui_module.service.ClientService;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

	private final RestClient restClient;

	@Override
	public ClientDto getClientById(Long id) {

		log.info("Получен запрос на получение пользователя с id: {}", id);

		ClientDto result = restClient.get()
                .uri("/clients/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
                .body(ClientDto.class);

		log.info("Пользователь с id: {} успешно получен", id);

		return result;
	}

	@Override
	public Stream<ClientDto> getAllClients() {

		log.info("Получен запрос на получение всех пользователей");

		List<ClientDto> clients = restClient.get()
		.uri("/clients")
		.retrieve()
		.onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
		.onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
		.body(new ParameterizedTypeReference<List<ClientDto>>() {});

		Stream<ClientDto> result = clients != null 
		? clients.stream()
		: Stream.empty();

		return result;
	}

	@Override
	public Optional<ClientDto> createClient(ClientDto clientDto) {

		log.info("Получен запрос на создание пользователя: {}", clientDto);

		try {

			ClientDto created = restClient.post()
			.uri("/clients")
			.body(clientDto)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        int statusCode = res.getStatusCode().value();
                        log.warn("⚠️ Клиентская ошибка при создании: HTTP {}", statusCode);

                        if (statusCode == 400) {
                            // Специальная обработка валидации: читаем тело и парсим ошибку
                            try {
                                String body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                                log.debug("📄 Тело ошибки валидации: {}", body);

                                ValidationErrorResponse validationError = new ObjectMapper()
                                        .readValue(body, ValidationErrorResponse.class);

                                // Выбрасываем предметное исключение с деталями валидации
                                throw new ValidationException(
                                        validationError.getMessage(),
                                        validationError.getAllErrorMessages()
                                );
                            } catch (IOException | JacksonException e) {
                                log.error("❌ Не удалось распарсить ошибку валидации", e);
                                throw new ValidationException(
                                        "Ошибка валидации",
                                        List.of("Не удалось прочитать детали ошибки")
                                );
                            }
                        }

                        // Для 409 (Conflict) и 422 (Unprocessable) — не выбрасываем исключение,
                        // позволяем методу завершиться и вернуть Optional.empty()
                        if (statusCode == 409 || statusCode == 422) {
                            log.info("ℹ️ Статус {}: пользователь не создан (возможно, уже существует)", statusCode);
                            return; // Прерываем обработку, не выбрасывая исключение
                        }

                        // Все остальные 4xx — стандартная обработка
                        handleErrorResponse(req, res);
                    })
					.onStatus(HttpStatusCode::is5xxServerError, this::handleErrorResponse)
					.body(ClientDto.class);

			if (created != null) {
				log.info("✅ Пользователь успешно создан: id={}, name={}",
								created.getClientId(), created.getClientName());
				return Optional.of(created);
		} else {
				log.info("⚪ Пользователь не создан (ответ пустой)");
				return Optional.empty();
		}
			
		} catch (ValidationException | ExternalApiException e) {
				throw e;
		}
		catch (Exception e) {
			log.error("Ошибка при создании пользователя: {}", e);
			throw new ExternalApiException(
				"Ошибка при создании пользователя", 
				null, 
				null, 
				e);
		}
	}

	@Override
	public Optional<ClientDto> updateClient(Long clientId, ClientDto clientDto) {
		return null;
	}
	
	@Override
	public void deleteClient(Long clientId) {
		return null;
	}

	@Override
	public Stream<ClientDto> searchClients(String clientName, String type, String address) {
		return null;
	}

	private void handleErrorResponse(HttpRequest request, ClientHttpResponse response) {
		HttpStatusCode code = null;

		try {
				code = response.getStatusCode();
				// Читаем тело ответа для включения в исключение (отладка/логирование)
				String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

				String errorType = code.is4xxClientError() ? "🔴 Client error" : "🔥 Server error";
				log.error("{}: {} | URL: {} | Тело: {}",
								errorType, code, request.getURI(), body);

				// Выбрасываем предметное исключение с полной информацией об ошибке
				throw new ExternalApiException(
								errorType + ": " + code,
								code,
								body
				);

		} catch (IOException e) {
				// Если не удалось прочитать тело ответа (например, оно уже прочитано)
				log.error("❌ Не удалось прочитать тело ошибки для статуса {}", code, e);

				throw new ExternalApiException(
								"Failed to read error response",
								code,
								null,
								e // Сохраняем исходное исключение как причину
				);
		}
}
}
