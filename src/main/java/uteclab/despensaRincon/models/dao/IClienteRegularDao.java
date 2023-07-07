package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.ClienteRegular;

import java.util.List;
import java.util.Optional;

public interface IClienteRegularDao extends CrudRepository<ClienteRegular, Long> {
    @Query("SELECT c.correo FROM ClienteRegular c WHERE c.correo LIKE '%@%.%' AND c.correo IS NOT NULL AND c.correo <> ''")
    public List<String> findAllCorreos();

    public  List<ClienteRegular> findByCedulaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String cedula,String nombre, String correo);


    Optional<ClienteRegular> findByCedula(String cedula);
}
