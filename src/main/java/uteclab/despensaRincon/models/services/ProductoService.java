package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.dao.IProductoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public List<Producto> findAll() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    public List<Producto> findBajoStock() {
        return productoDao.findBajoStock();
    }

    @Override
    public Producto findById(Long id) {
        return productoDao.findById(id).orElse(null);
    }
    @Override
    public List<Producto>  findByProveedor (Long proveedor_id) {
        return productoDao.findByProveedor(proveedor_id);
    }

    @Override
    public Producto findByNombre(String nombre) {
        return productoDao.findByNombre(nombre);
    }

    @Override
    public List<Producto> buscadorNombre(String nombre) {
        return productoDao.findByNombreContainingIgnoreCase(nombre.toLowerCase());
    }

    @Override
    public Producto save(Producto pr) {
        return productoDao.save(pr);
    }

    @Override
    public void deleteById(Long id) {
            productoDao.deleteById(id);
    }

    @Override
    public ResponseEntity<?> buscador(String query, Long categoria_id, Boolean visible){

        Map<String, Object> map = new HashMap<>();

        if(categoria_id == 0){
            if(query.isEmpty() || query.isBlank()){

                List<Producto> res = this.findAll();

                if (visible) {
                    res.removeIf(producto -> !producto.isVisible());
                }

                map.put("productos", res);
                map.put("msg", "Se han encontrado " + res.size() + " productos");
                return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
            }
            else{
                List<Producto> res = this.buscadorNombre(query);

                if (visible) {
                    res.removeIf(producto -> !producto.isVisible());
                }

                map.put("productos", res);
                map.put("msg", "Se han encontrado " + res.size() + " productos");
                return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
            }

        }

        // Se filtró por una categoria
        else{
            Categoria c = categoriaService.findById(categoria_id);

            //No existe una categoria con dicha id
            if(c == null){
                map.put("msg", "Hubo un error al realizar la busqueda");
                map.put("error", "No existe una categoria con id = " + categoria_id);
                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
            }
            // Obtengo todos los productos de dicha categoria y comienzo a buscar por "query"
            else{

                if(query.isBlank() || query.isEmpty()){

                    List<Producto> productos = c.getProductos();

                    if (visible) {
                        productos.removeIf(producto -> !producto.isVisible());
                    }

                    map.put("productos", productos);
                    map.put("msg", "Se han encontrado " + productos.size() + " productos");
                    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
                }
                else{
                    List<Producto> productos = c.getProductos();
                    List<Producto> res = new ArrayList<>();

                    for (Producto p: productos) {
                        if(p.getNombre().toLowerCase().contains(query.toLowerCase())){
                            res.add(p);
                        }
                    }

                    if (visible) {
                        productos.removeIf(producto -> !producto.isVisible());
                    }

                    map.put("productos", res);
                    map.put("msg", "Se han encontrado " + res.size() + " productos");
                    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);

                }
            }
        }
    }


}
