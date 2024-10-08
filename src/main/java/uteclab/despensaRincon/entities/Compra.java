package uteclab.despensaRincon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String comentario;
    private Float total;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "compra_id")
    @NotNull
    private List<LineaCompra> lineasCompra;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="proveedor_id", nullable = true)
    private Proveedor proveedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public List<LineaCompra> getLineasCompra() {
        return lineasCompra;
    }

    public void setLineasCompra(List<LineaCompra> lineasCompra) {
        this.lineasCompra = lineasCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
