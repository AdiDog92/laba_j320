package src.backend_module.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.backend_module.model.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
