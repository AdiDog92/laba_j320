package src.backend_module.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
	Long id;

	@NotBlank(message = "IP-адрес не может быть пустым")
	@Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$",
					message = "Неверный формат IPv4 (допустимо: 0.0.0.0 – 255.255.255.255)")
	String ip;

	@NotBlank(message = "Mac-адрес не может быть пустым")
	@Pattern(regexp = "^([0-9A-Fa-f]{2}-){5}[0-9A-Fa-f]{2}$",
					message = "Неверный формат. Ожидается: XX-XX-XX-XX-XX-XX (0-9, A-F)")
	String mac;

	@NotBlank(message = "Модель устройства должна быть заполнена")
	@Size(max = 100, message = "Длинна не более 100 символов")
	String model;

	@NotBlank(message = "Должен быть указан адрес")
	@Size(max = 200, message = "Длинна не более 200 символов")
	String address;
}
