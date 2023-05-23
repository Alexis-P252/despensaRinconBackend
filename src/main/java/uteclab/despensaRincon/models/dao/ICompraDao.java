package uteclab.despensaRincon.models.dao;
import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Compra;

public interface ICompraDao extends CrudRepository <Compra, Long> {
}
