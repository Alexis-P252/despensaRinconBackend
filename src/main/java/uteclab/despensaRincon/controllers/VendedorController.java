package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.services.VendedorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/vendedor")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
public class VendedorController {
    @Autowired
    private VendedorService vendedorService;

    @GetMapping("")
    public List<Vendedor> findAll() {
        return vendedorService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){

        Vendedor ve = null;
        Map<String, Object> response = new HashMap<>();
        try {
            ve=vendedorService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ve==null){
            response.put("msg","No existe un vendedor con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Vendedor>(ve,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Vendedor vendedor)     {
        Vendedor newVendedor =null;
        Map <String,Object> response = new HashMap<>();

        if(!vendedor.getTelefono().matches("^\\+?[0-9]{8,}$")){
            response.put("msg", "El telefono ingresado no tiene un formato valido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(!vendedor.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            response.put("msg", "El correo ingresado no tiene un formato valido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            newVendedor = vendedorService.save(vendedor);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar ingresar el Vendedor");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Datos del Vendedor ingresados correctamente");
        response.put("vendedor", newVendedor);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Vendedor vendedor, BindingResult result, @PathVariable(value="id")Long id ) {
        Vendedor vendedorActual = vendedorService.findById(id);

        Map<String,Object> response = new HashMap<>();

        if(vendedorActual == null) {
            response.put("msg","No existe un vendedor con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(!vendedor.getTelefono().matches("^\\+?[0-9]{8,}$")){
            response.put("msg", "El telefono ingresado no tiene un formato valido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(!vendedor.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            response.put("msg", "El correo ingresado no tiene un formato valido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("In the field: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("errors", errors);
            response.put("msg", "Error al validar el vendedor");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }
        vendedorActual.setNombre(vendedor.getNombre());
        vendedorActual.setCorreo(vendedor.getCorreo());
        vendedorActual.setTelefono(vendedor.getTelefono());

        try {
            vendedorActual = vendedorService.save(vendedorActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el vendedor");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Producto actualizado correctamente");
        response.put("producto", vendedorActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }



}
