package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Vendedor;

import java.util.List;

public interface IVendedorDao extends CrudRepository<Vendedor,Long> {
    public List<Vendedor> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);
}
