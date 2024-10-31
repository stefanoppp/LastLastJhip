package um.prog2.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import um.prog2.domain.Dispositivo;

/**
 * Spring Data JPA repository for the Dispositivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    Dispositivo findByIdExterno(Long idExterno);

    boolean existsByIdExterno(Long idExterno);
}
