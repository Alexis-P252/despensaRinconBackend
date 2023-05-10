package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.ClienteRegular;
public interface IClienteRegularDao extends CrudRepository<ClienteRegular, Long> {
    public ClienteRegular update(ClienteRegular cr);
}
