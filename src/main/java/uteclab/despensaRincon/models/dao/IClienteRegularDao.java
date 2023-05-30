package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.ClienteRegular;

import java.util.List;
import java.util.Optional;

public interface IClienteRegularDao extends CrudRepository<ClienteRegular, Long> {

    Optional<ClienteRegular> findByCedula(String cedula);
}
