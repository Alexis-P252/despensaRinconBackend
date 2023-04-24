package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.entities.Producto;

import java.util.List;

public interface IProductoService {
    public List<Producto> findAll();
    //// aca tengo una duda
    public Producto findById(Long id);
    public Producto save (Producto em);
    public void deleteById (Long id);
}
