package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Proveedor;

public interface IProveedorDao extends CrudRepository<Proveedor,Long> {
}
