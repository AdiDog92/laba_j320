package src.backend_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.backend_module.model.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}
