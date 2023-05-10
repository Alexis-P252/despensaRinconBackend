package uteclab.despensaRincon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


@Entity
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @NotEmpty
    private String nombre;
    private String telefono;
    private String  correo;
    @ManyToMany
    @JsonIgnore
    private List<Proveedor> proveedores;

    public Long getId() {
        return id;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
