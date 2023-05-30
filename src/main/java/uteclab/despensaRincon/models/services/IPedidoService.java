package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Pedido;

import java.util.List;

public interface IPedidoService {

    public List<Pedido> findAll();
    public List<Pedido> findByFinalizadoFalse();
    public Pedido findById(Long id);
    public Pedido save (Pedido pedido);
    public void deleteById (Long id);

}
