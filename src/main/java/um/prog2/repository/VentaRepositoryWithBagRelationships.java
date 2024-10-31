package um.prog2.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import um.prog2.domain.Venta;

public interface VentaRepositoryWithBagRelationships {
    Optional<Venta> fetchBagRelationships(Optional<Venta> venta);

    List<Venta> fetchBagRelationships(List<Venta> ventas);

    Page<Venta> fetchBagRelationships(Page<Venta> ventas);
}
