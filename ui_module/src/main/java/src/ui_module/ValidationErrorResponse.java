package src.ui_module;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationErrorResponse {

    private String message;

    @JsonProperty("errorDescriptions")
    private List<ErrorDescription> errorDescriptions;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDescription {
        @JsonProperty("errorMessage")
        private String errorMessage;
    }

    public List<String> getAllErrorMessages() {
        return errorDescriptions != null
                ? errorDescriptions.stream()
                .map(ErrorDescription::getErrorMessage)
                .toList()
                : List.of();
    }
}
