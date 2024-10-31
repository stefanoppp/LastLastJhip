package um.prog2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import um.prog2.domain.Venta;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class VentaRepositoryWithBagRelationshipsImpl implements VentaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String VENTAS_PARAMETER = "ventas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Venta> fetchBagRelationships(Optional<Venta> venta) {
        return venta.map(this::fetchPersonalizaciones).map(this::fetchAdicionales);
    }

    @Override
    public Page<Venta> fetchBagRelationships(Page<Venta> ventas) {
        return new PageImpl<>(fetchBagRelationships(ventas.getContent()), ventas.getPageable(), ventas.getTotalElements());
    }

    @Override
    public List<Venta> fetchBagRelationships(List<Venta> ventas) {
        return Optional.of(ventas).map(this::fetchPersonalizaciones).map(this::fetchAdicionales).orElse(Collections.emptyList());
    }

    Venta fetchPersonalizaciones(Venta result) {
        return entityManager
            .createQuery("select venta from Venta venta left join fetch venta.personalizaciones where venta.id = :id", Venta.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Venta> fetchPersonalizaciones(List<Venta> ventas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ventas.size()).forEach(index -> order.put(ventas.get(index).getId(), index));
        List<Venta> result = entityManager
            .createQuery("select venta from Venta venta left join fetch venta.personalizaciones where venta in :ventas", Venta.class)
            .setParameter(VENTAS_PARAMETER, ventas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Venta fetchAdicionales(Venta result) {
        return entityManager
            .createQuery("select venta from Venta venta left join fetch venta.adicionales where venta.id = :id", Venta.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Venta> fetchAdicionales(List<Venta> ventas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ventas.size()).forEach(index -> order.put(ventas.get(index).getId(), index));
        List<Venta> result = entityManager
            .createQuery("select venta from Venta venta left join fetch venta.adicionales where venta in :ventas", Venta.class)
            .setParameter(VENTAS_PARAMETER, ventas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
