package um.prog2.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import um.prog2.domain.Caracteristica;

/**
 * Spring Data JPA repository for the Caracteristica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    Caracteristica findByIdExterno(Long idExterno);

    boolean existsByIdExterno(Long idExterno);
}
