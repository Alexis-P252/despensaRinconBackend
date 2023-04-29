package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
    public Producto findByNombre(String nombre);
}
