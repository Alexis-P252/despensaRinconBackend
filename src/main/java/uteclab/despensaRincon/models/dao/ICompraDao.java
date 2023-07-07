package uteclab.despensaRincon.models.dao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uteclab.despensaRincon.entities.Compra;

import java.util.List;

public interface ICompraDao extends CrudRepository <Compra, Long> {
    @Query("SELECT c FROM Compra c WHERE c.proveedor.id = :pr_id ")
    List<Compra> findByProveedor (@Param("pr_id") Long proveedor_id);
}
