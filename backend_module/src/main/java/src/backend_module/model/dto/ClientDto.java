package src.backend_module.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class ClientDto {

		@JsonProperty(value = "id")
    private Long clientId;

		@JsonProperty(value = "client_name")
    @NotBlank(message = "Имя клиента не должно быть пустым")
    @Size(max = 100, message = "Длинна не более 100 символов")
    @Pattern(regexp = "^[а-яА-ЯёЁ \\.\\-]+$",
            message = "Только русские буквы, дефис, точка, пробел")
    private String clientName;

    @NotBlank(message = "Тип не может быть пустым")
    @Pattern(regexp = "^(Юридическое лицо|Физическое лицо)$",
            message = "Допустимые значения: «Юридическое лицо» или «Физическое лицо»\"")
    private String type;

    private LocalDate added;

    @NotEmpty(message = "Адреса не могут быть пустыми")
    private List<AddressDto> addresses;

}
