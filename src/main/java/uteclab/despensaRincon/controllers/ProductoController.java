package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.services.ProductoService;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/dr/producto")
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
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> findByNombre(@PathVariable(value="nombre") String nombre){
        Producto pr = null;
        Map<String, Object> response = new HashMap<>();
        try {
            pr=productoService.findByNombre(nombre);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (pr==null){
            response.put("msg","No existe un producto con ese nombre");
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
        response.put("producto", newProducto);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult result, @PathVariable(value="id")Long id ) {

        Producto productoActual = productoService.findById(id);

        Map<String,Object> response = new HashMap<>();

        if(productoActual == null) {
            response.put("msg","No existe un producto con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("In the field: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("errors", errors);
            response.put("msg", "Error al validar el producto");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }

        productoActual.setNombre(producto.getNombre());
        productoActual.setCategoria(producto.getCategoria());
        productoActual.setImagen(producto.getImagen());
        productoActual.setDescripcion(producto.getDescripcion());
        productoActual.setPrecio_compra(producto.getPrecio_compra());
        productoActual.setPrecio_venta(producto.getPrecio_venta());
        productoActual.setStock_minimo(producto.getStock_minimo());
        productoActual.setVisible(producto.isVisible());


        try {
            productoActual = productoService.save(productoActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el producto");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Producto actualizado correctamente");
        response.put("producto", productoActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }


}
