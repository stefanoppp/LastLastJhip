package um.prog2.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link um.prog2.domain.Opcion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OpcionDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private Long idExterno;

    @NotNull
    private String codigo;

    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precioAdicional;

    @NotNull
    private PersonalizacionDTO personalizacion;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public BigDecimal getPrecioAdicional() {
        return precioAdicional;
    }

    public void setPrecioAdicional(BigDecimal precioAdicional) {
        this.precioAdicional = precioAdicional;
    }

    public PersonalizacionDTO getPersonalizacion() {
        return personalizacion;
    }

    public void setPersonalizacion(PersonalizacionDTO personalizacion) {
        this.personalizacion = personalizacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpcionDTO)) {
            return false;
        }

        OpcionDTO opcionDTO = (OpcionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, opcionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpcionDTO{" +
            "id=" + getId() +
            ", idExterno=" + getIdExterno() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioAdicional=" + getPrecioAdicional() +
            ", personalizacion=" + getPersonalizacion() +
            "}";
    }
}
