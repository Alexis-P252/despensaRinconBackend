package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Proveedor;

import java.util.List;

public interface IProveedorDao extends CrudRepository<Proveedor,Long> {
    public List<Proveedor> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);
}
