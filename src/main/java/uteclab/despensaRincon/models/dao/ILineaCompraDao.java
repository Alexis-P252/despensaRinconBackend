package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.LineaCompra;

public interface ILineaCompraDao extends CrudRepository<LineaCompra, Long> {
}
