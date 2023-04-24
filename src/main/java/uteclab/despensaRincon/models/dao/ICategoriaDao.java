package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Categoria;

public interface ICategoriaDao extends CrudRepository<Categoria, Long> {
}
