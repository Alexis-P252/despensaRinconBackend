package uteclab.despensaRincon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class LineaVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long cantidad;
    private Float precio;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
