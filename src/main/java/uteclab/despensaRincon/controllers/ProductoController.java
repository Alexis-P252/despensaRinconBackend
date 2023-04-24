package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.services.ProductoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/dr/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public List<Producto> findAll() {
        return productoService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Producto pr = null;
        Map<String, Object> response = new HashMap<>();
        try {
            pr=productoService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (pr==null){
            response.put("msg","No existe un producto con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Producto>(pr,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Producto producto)     {
        Producto newProducto =null;
        Map <String,Object> response = new HashMap<>();

        try{
            newProducto = productoService.save(producto);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar el Producto");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Datos del Producto ingresados correctamente");
        response.put("Producto", newProducto);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


}
