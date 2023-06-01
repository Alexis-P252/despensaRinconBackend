package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Producto;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {
    public Producto findByNombre(String nombre);

    // SELECT * FROM producto WHERE LOWER(nombre) LIKE
    public List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.stock < p.stock_minimo")
    public List<Producto> findBajoStock();
}
