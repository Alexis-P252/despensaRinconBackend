package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.services.ProductoService;
import uteclab.despensaRincon.models.services.ProveedorService;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/dr/producto")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("")
    public List<Producto> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/bajoStock")
    public List<Producto> findBajoStock(){
        return productoService.findBajoStock();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Producto pr = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try {
            pr=productoService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
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
        List<String> error = new ArrayList<>();
        try {
            pr=productoService.findByNombre(nombre);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (pr==null){
            response.put("msg","No existe un producto con ese nombre");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Producto>(pr,HttpStatus.OK);
    }

    @GetMapping("buscar/{query}/{categoria}/{soloVisible}")
    public ResponseEntity<?> buscador(@RequestParam(value = "query", required = false) String query, @RequestParam(value = "categoria", required = false) Long categoria, @RequestParam(value="soloVisible", required=false) Boolean visible){

        // Si query es vacio o null, se traen todos los productos
        // Si categoria es null o 0, no se filtra por ninguna categoria, de lo contrario se filtra por el id de la categoria.

        if(query==null){
            query = "";
        }
        if(categoria==null){
            categoria = 0L;
        }

        if(visible == null){
            visible = false;
        }

        return productoService.buscador(query,categoria, visible);

    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Producto producto)     {
        Producto newProducto =null;
        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try{
            newProducto = productoService.save(producto);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar el Producto");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
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
        List<String> error = new ArrayList<>();

        if(productoActual == null) {
            response.put("msg","No existe un producto con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("In the field: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
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
        if(producto.getProveedores() != null){
            productoActual.setProveedores(producto.getProveedores());
        }


        try {
            productoActual = productoService.save(productoActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el producto");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Producto actualizado correctamente");
        response.put("producto", productoActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/agregarProveedor/{id}")
    public ResponseEntity<?> agregarProveedor(@Valid @RequestBody Proveedor proveedor, BindingResult result, @PathVariable(value="id")Long id){
        Producto productoActual = productoService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(productoActual == null) {
            response.put("msg","No existe un producto con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(proveedorService.findById(proveedor.getId()) == null){
            response.put("msg","No existe un proveedor con id = ".concat(proveedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        Boolean existe = false;
        List<Proveedor> proveedoresActual = productoActual.getProveedores();
        for (int i = 0; i < proveedoresActual.size(); i++) {
            if (proveedoresActual.get(i).getId() == proveedor.getId()) {
                existe = true;
            }
        }
        if(existe){
            response.put("msg","El producto ya tiene al proveedor con id = ".concat(proveedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        productoActual.getProveedores().add(proveedor);
        try {
            productoActual = productoService.save(productoActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el producto");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Proveedor asociado correctamente al producto");
        response.put("producto", productoActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }

    @PutMapping("/quitarProveedor/{id}")
    public ResponseEntity<?> quitarProveedor(@Valid @RequestBody Proveedor proveedor, BindingResult result, @PathVariable(value="id")Long id ) {

        Producto productoActual = productoService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(productoActual == null) {
            response.put("msg","No existe un producto con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        //este control creo que no es necesario
        if(proveedorService.findById(proveedor.getId()) == null){
            response.put("msg","No existe un proveedor con id = ".concat(proveedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        Boolean existe = false;
        List<Proveedor> proveedoresActual = productoActual.getProveedores();
        for (int i = 0; i < proveedoresActual.size(); i++) {
            if (proveedoresActual.get(i).getId() == proveedor.getId()) {
                proveedoresActual.remove(i);
                existe = true;
            }
        }
        if(!existe){
            response.put("msg","El producto no tiene un proveedor con id = ".concat(proveedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        productoActual.setProveedores(proveedoresActual);
        try {
            productoActual = productoService.save(productoActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el producto");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Proveedor quitado correctamente");
        response.put("producto", productoActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(productoService.findById(id) != null ){
            try{
                productoService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar el producto");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Producto eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error al intentar eliminar el producto");
            error.add("No existe un producto con id = " + id );
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }


}
