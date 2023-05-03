package uteclab.despensaRincon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
public class RegistroDeuda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private Date fecha;
    @NotEmpty
    private Float monto;
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

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public ClienteRegular getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRegular cliente) {
        this.cliente = cliente;
    }
}
