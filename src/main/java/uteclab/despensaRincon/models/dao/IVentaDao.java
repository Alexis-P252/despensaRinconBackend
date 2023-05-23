package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Venta;

public interface IVentaDao extends CrudRepository<Venta,Long> {
}
