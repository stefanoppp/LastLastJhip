package um.prog2.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import um.prog2.domain.Opcion;

/**
 * Spring Data JPA repository for the Opcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    Opcion findByIdExterno(Long idExterno);
}
