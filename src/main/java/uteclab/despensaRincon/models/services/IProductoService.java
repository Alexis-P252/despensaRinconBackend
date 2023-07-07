package uteclab.despensaRincon.models.services;

import org.springframework.http.ResponseEntity;
import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.entities.Producto;

import java.util.List;
import java.util.Map;

public interface IProductoService {
    public List<Producto> findAll();
    //// aca tengo una duda

    public List<Producto> findBajoStock();
    public Producto findById(Long id);
    public Producto findByNombre(String nombre);

    public List<Producto> buscadorNombre(String nombre);

    public Producto save (Producto em);
    public void deleteById (Long id);

    public ResponseEntity<?> buscador(String query, Long categoria_id, Boolean visible);
    public List<Producto>  findByProveedor (Long proveedor_id);
}
