package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Aviso;

import java.util.List;

public interface IAvisoDao extends CrudRepository<Aviso,Long> {
    public List<Aviso> findByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String titulo, String descripcion);
}
