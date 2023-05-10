package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Vendedor;

public interface IVendedorDao extends CrudRepository<Vendedor,Long> {
}
