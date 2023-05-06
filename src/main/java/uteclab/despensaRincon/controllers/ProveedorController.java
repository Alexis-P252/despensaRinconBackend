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
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.services.ProveedorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    @GetMapping("")
    public List<Proveedor> findAll() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Proveedor proveedor = null;
        Map<String, Object> response = new HashMap<>();
        try {
            proveedor = proveedorService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (proveedor==null){
            response.put("msg","No existe un proveedor con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Proveedor>(proveedor,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Proveedor proveedor)     {
        Proveedor newProveedor =null;
        Map <String,Object> response = new HashMap<>();

        try{
            newProveedor = proveedorService.save(proveedor);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar ingresar el Proveedor");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Datos del Proveeedor ingresados correctamente");
        response.put("proveedor", newProveedor);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Proveedor proveedor, BindingResult result, @PathVariable(value="id")Long id ) {

        Proveedor proveedorActual = proveedorService.findById(id);

        Map<String,Object> response = new HashMap<>();

        if(proveedorActual == null) {
            response.put("msg","No existe un proveedor con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("In the field: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("errors", errors);
            response.put("msg", "Error al validar el proveedor");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }

        proveedorActual.setNombre(proveedor.getNombre());
        proveedorActual.setDireccion(proveedor.getDireccion());
        proveedorActual.setCorreo(proveedor.getCorreo());
        proveedorActual.setImagen(proveedor.getImagen());
        proveedorActual.setVendedores(proveedor.getVendedores());
        try {
            proveedorActual = proveedorService.save(proveedorActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el proveedor");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Proveedor actualizado correctamente");
        response.put("proveedor", proveedorActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();

        if(proveedorService.findById(id) != null ){
            try{
                proveedorService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar la proveedor");
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Proveedor eliminada correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error tal intentar eliminar al proveedor");
            response.put("error", "No existe un proveedor con id = " + id);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }

}
