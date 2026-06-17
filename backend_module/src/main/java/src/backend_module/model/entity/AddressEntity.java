package src.backend_module.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ip", nullable = false)
    @NotBlank(message = "IP-адрес не может быть пустым")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$",
            message = "Неверный формат IPv4 (допустимо: 0.0.0.0 – 255.255.255.255)")
    private String ip;

    @Column(name = "mac", nullable = false)
    @NotBlank(message = "Mac-адрес не может быть пустым")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}-){5}[0-9A-Fa-f]{2}$",
            message = "Неверный формат. Ожидается: XX-XX-XX-XX-XX-XX (0-9, A-F)")
    private String mac;

    @Column(name = "model", nullable = false, length = 100)
    @NotBlank(message = "Модель устройства должна быть заполнена")
    @Size(max = 100, message = "Длинна не более 100 символов")
    private String model;

    @Column(name = "address", nullable = false, length = 200)
    @NotBlank(message = "Должен быть указан адрес")
    @Size(max = 200, message = "Длинна не более 200 символов")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private ClientEntity client;
}
