package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.RegistroDeuda;

public interface IRegistroDeudaDao extends CrudRepository<RegistroDeuda,Long>{}
