package src.ui_module.model;

import lombok.Getter;

@Getter
public enum ClientTypeEnum {
	INDIVIDUAL("Физическое лицо"),
	BUSINESS("Юридическое лицо");

	private final String description;

	ClientTypeEnum(String description) {
		this.description = description;
	}

	public static ClientTypeEnum getClientType(String type){

		if(type == null || type.isBlank()) {
			return null;
		}

		for (ClientTypeEnum value : values()) {
			if(value.getDescription().equalsIgnoreCase(type)) {
				return value;
			}
		}

		return null;
	}
}
