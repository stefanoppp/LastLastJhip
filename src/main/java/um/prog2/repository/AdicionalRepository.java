package um.prog2.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import um.prog2.domain.Adicional;

/**
 * Spring Data JPA repository for the Adicional entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
    Adicional findByIdExterno(Long idExterno);

    boolean existsByIdExterno(Long idExterno);
}
