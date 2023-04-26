package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Aviso;

public interface IAvisoDao extends CrudRepository<Aviso,Long> {
}
