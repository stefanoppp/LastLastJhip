package um.prog2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private ZonedDateTime fechaVenta;

    @NotNull
    @Column(name = "precio_final", nullable = false)
    private Float precioFinal;

    @ManyToOne(optional = false)
    @NotNull
    private Dispositivo dispositivo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_venta__personalizaciones",
        joinColumns = @JoinColumn(name = "venta_id"),
        inverseJoinColumns = @JoinColumn(name = "personalizaciones_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dispositivo", "ventas" }, allowSetters = true)
    private Set<Personalizacion> personalizaciones = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_venta__adicionales",
        joinColumns = @JoinColumn(name = "venta_id"),
        inverseJoinColumns = @JoinColumn(name = "adicionales_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dispositivo", "ventas" }, allowSetters = true)
    private Set<Adicional> adicionales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFechaVenta() {
        return this.fechaVenta;
    }

    public Venta fechaVenta(ZonedDateTime fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(ZonedDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Float getPrecioFinal() {
        return this.precioFinal;
    }

    public Venta precioFinal(Float precioFinal) {
        this.setPrecioFinal(precioFinal);
        return this;
    }

    public void setPrecioFinal(Float precioFinal) {
        this.precioFinal = precioFinal;
    }

    public Dispositivo getDispositivo() {
        return this.dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Venta dispositivo(Dispositivo dispositivo) {
        this.setDispositivo(dispositivo);
        return this;
    }

    public Set<Personalizacion> getPersonalizaciones() {
        return this.personalizaciones;
    }

    public void setPersonalizaciones(Set<Personalizacion> personalizacions) {
        this.personalizaciones = personalizacions;
    }

    public Venta personalizaciones(Set<Personalizacion> personalizacions) {
        this.setPersonalizaciones(personalizacions);
        return this;
    }

    public Venta addPersonalizaciones(Personalizacion personalizacion) {
        this.personalizaciones.add(personalizacion);
        return this;
    }

    public Venta removePersonalizaciones(Personalizacion personalizacion) {
        this.personalizaciones.remove(personalizacion);
        return this;
    }

    public Set<Adicional> getAdicionales() {
        return this.adicionales;
    }

    public void setAdicionales(Set<Adicional> adicionals) {
        this.adicionales = adicionals;
    }

    public Venta adicionales(Set<Adicional> adicionals) {
        this.setAdicionales(adicionals);
        return this;
    }

    public Venta addAdicionales(Adicional adicional) {
        this.adicionales.add(adicional);
        return this;
    }

    public Venta removeAdicionales(Adicional adicional) {
        this.adicionales.remove(adicional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return getId() != null && getId().equals(((Venta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioFinal=" + getPrecioFinal() +
            "}";
    }
}
