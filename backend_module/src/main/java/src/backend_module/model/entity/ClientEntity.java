package src.backend_module.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "client", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id", nullable = false)
	private Long clientId;

	@Column(name = "client_name", length = 100, nullable = false)
	@NotBlank(message = "Имя клиента не должно быть пустым")
	@Size(max = 100, message = "Длинна не более 100 символов")
	@Pattern(regexp = "^[а-яА-ЯёЁ \\.\\-]+$",
						message = "Только русские буквы, дефис, точка, пробел")
	private String clientName;

	@Column(name = "type", nullable = false)
	@NotBlank(message = "Тип не может быть пустым")
	@Pattern(regexp = "^(Юридическое лицо|Физическое лицо|юридическое лицо|физическое лицо)$",
	message = "Допустимые значения: «Юридическое лицо» или «Физическое лицо»\"")
	private String type;

	@CreatedDate
	@Column(name = "added")
	private LocalDate added;

	@OneToMany(
					mappedBy = "client",
					fetch = FetchType.LAZY ,
					cascade = CascadeType.ALL
	)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<AddressEntity> addresses = new LinkedHashSet<>();
}
