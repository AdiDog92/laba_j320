package src.backend_module.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import src.backend_module.model.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query(value = """
                SELECT c.*
                FROM public.client c
                WHERE (:client_name IS NULL OR c.client_name ILIKE :client_name)
                  AND (:type IS NULL OR c.type ILIKE :type)
                  AND (:address IS NULL OR EXISTS (
                      SELECT 1 FROM public.address a 
                      WHERE a.client_id = c.client_id 
                      AND CAST(a.address AS TEXT) ILIKE :address
                  ))
                """, nativeQuery = true)
    List<ClientEntity> searchByParam(
            @Param("client_name") String clientName,
            @Param("type") String type,
            @Param("address") String address
    );

}
