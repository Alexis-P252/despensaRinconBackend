package uteclab.despensaRincon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String contenido;
    private Boolean finalizado;

    @ManyToOne
    private ClienteRegular cliente;

    @OneToMany
    private List<Producto> productos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public ClienteRegular getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRegular cliente) {
        this.cliente = cliente;
    }
}
