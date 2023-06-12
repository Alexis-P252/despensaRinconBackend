package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.entities.Producto;

import java.util.List;

public interface IEstadisticaDao extends CrudRepository<Producto, Long> {

    @Query("SELECT new uteclab.despensaRincon.entities.Estadistica(c.nombre, COUNT(p.id)) FROM Categoria c JOIN c.productos p GROUP BY c.nombre")

    public List<Estadistica> cantProductosPorCategoria();


}
