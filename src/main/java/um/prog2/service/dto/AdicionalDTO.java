package um.prog2.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link um.prog2.domain.Adicional} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdicionalDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Long idExterno;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precio;

    @NotNull
    @DecimalMin(value = "-1")
    private BigDecimal precioGratis;

    @NotNull
    private DispositivoDTO dispositivo;

    private Set<VentaDTO> ventas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(Long idExterno) {
        this.idExterno = idExterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioGratis() {
        return precioGratis;
    }

    public void setPrecioGratis(BigDecimal precioGratis) {
        this.precioGratis = precioGratis;
    }

    public DispositivoDTO getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(DispositivoDTO dispositivo) {
        this.dispositivo = dispositivo;
    }

    public Set<VentaDTO> getVentas() {
        return ventas;
    }

    public void setVentas(Set<VentaDTO> ventas) {
        this.ventas = ventas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdicionalDTO)) {
            return false;
        }

        AdicionalDTO adicionalDTO = (AdicionalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adicionalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdicionalDTO{" +
            "id=" + getId() +
            ", idExterno=" + getIdExterno() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", precioGratis=" + getPrecioGratis() +
            ", dispositivo=" + getDispositivo() +
            ", ventas=" + getVentas() +
            "}";
    }
}
