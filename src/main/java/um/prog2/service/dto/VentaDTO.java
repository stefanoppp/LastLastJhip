package um.prog2.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link um.prog2.domain.Venta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime fechaVenta;

    @NotNull
    private Float precioFinal;

    @NotNull
    private DispositivoDTO dispositivo;

    private Set<PersonalizacionDTO> personalizaciones = new HashSet<>();

    private Set<AdicionalDTO> adicionales = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(ZonedDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Float getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Float precioFinal) {
        this.precioFinal = precioFinal;
    }

    public DispositivoDTO getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(DispositivoDTO dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Set<PersonalizacionDTO> getPersonalizaciones() {
        return personalizaciones;
    }

    public void setPersonalizaciones(Set<PersonalizacionDTO> personalizaciones) {
        this.personalizaciones = personalizaciones;
    }

    public Set<AdicionalDTO> getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(Set<AdicionalDTO> adicionales) {
        this.adicionales = adicionales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaDTO)) {
            return false;
        }

        VentaDTO ventaDTO = (VentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaDTO{" +
            "id=" + getId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioFinal=" + getPrecioFinal() +
            ", dispositivo=" + getDispositivo() +
            ", personalizaciones=" + getPersonalizaciones() +
            ", adicionales=" + getAdicionales() +
            "}";
    }
}
