package um.prog2.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import um.prog2.domain.Personalizacion;

/**
 * Spring Data JPA repository for the Personalizacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalizacionRepository extends JpaRepository<Personalizacion, Long> {
    Personalizacion findByIdExterno(Long idExterno);

    boolean existsByIdExterno(Long idExterno);
}
