package uteclab.despensaRincon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private Date fecha;
    private String comentario;
    private Float total;
    private Float montoDeuda;
    @OneToMany
    private List<LineaVenta> lineasVenta;
  //  @Column(nullable = true)
    @ManyToOne
    private ClienteRegular cliente;

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

    public Float getMontoDeuda() {
        return montoDeuda;
    }

    public void setMontoDeuda(Float montoDeuda) {
        this.montoDeuda = montoDeuda;
    }

    public List<LineaVenta> getLineasVenta() {
        return lineasVenta;
    }

    public void setLineasVenta(List<LineaVenta> lineasVenta) {
        this.lineasVenta = lineasVenta;
    }

    public ClienteRegular getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRegular cliente) {
        this.cliente = cliente;
    }
}
