package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Pedido;

import java.util.List;

public interface IPedidoDao extends CrudRepository<Pedido, Long> {

    List<Pedido> findByFinalizadoFalse();
}
