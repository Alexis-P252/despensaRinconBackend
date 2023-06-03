package uteclab.despensaRincon.models.dao;

import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Categoria;

import java.util.List;

public interface ICategoriaDao extends CrudRepository<Categoria, Long> {
    public Categoria findByNombre(String nombre);
    //esto es para buscar categoria por nombre o parte de el
    public List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
