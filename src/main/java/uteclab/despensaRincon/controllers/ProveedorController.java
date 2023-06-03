package uteclab.despensaRincon.controllers;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.backoff.BackOff;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.services.ProveedorService;
import uteclab.despensaRincon.models.services.VendedorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.tomcat.util.buf.Ascii.parseLong;

@RestController
@RequestMapping("/dr/proveedor")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private VendedorService vendedorService;

    @GetMapping("")
    public List<Proveedor> findAll() {
        return proveedorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Proveedor proveedor = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        try {
            proveedor = proveedorService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
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
        List<String> error = new ArrayList<>();
        if(!proveedor.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            response.put("msg", "El correo ingresado no tiene un formato valido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            newProveedor = proveedorService.save(proveedor);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar ingresar el Proveedor");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
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
        List<String> error = new ArrayList<>();

        if(proveedorActual == null) {
            response.put("msg","No existe un proveedor con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("In the field: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar el proveedor");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }

        if(!proveedor.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            response.put("msg", "El correo ingresado no tiene un formato valido");
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
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Proveedor actualizado correctamente");
        response.put("proveedor", proveedorActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(proveedorService.findById(id) != null ){
            try{
                proveedorService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar la proveedor");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Proveedor eliminada correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error tal intentar eliminar al proveedor");
            error.add("No existe un proveedor con id =" + id);
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping("/vendedor/{id}")
    public ResponseEntity<?> addVendedor(@Valid @RequestBody Vendedor vendedor, BindingResult result, @PathVariable(value="id")Long id ) {
        Proveedor proveedorActual = proveedorService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(proveedorActual == null) {
            response.put("msg","No existe un proveedor con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(vendedorService.findById(vendedor.getId()) == null){
            response.put("msg","No existe un vendedor con id = ".concat(vendedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        Boolean existe = false;
        List<Vendedor> vendedoresActual = proveedorActual.getVendedores();
        for (int i = 0; i < vendedoresActual.size(); i++) {
            if (vendedoresActual.get(i).getId() == vendedor.getId()) {
                existe = true;
            }
        }
        if(existe){
            response.put("msg","El proveedor ya tiene al vendedor con id = ".concat(vendedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        proveedorActual.getVendedores().add(vendedor);
        try {
            proveedorActual = proveedorService.save(proveedorActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el proveedor");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Vendedor agregado correctamente");
        response.put("proveedor", proveedorActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }
    @PutMapping("/vendedorQuitar/{id}")
    public ResponseEntity<?> removeVendedor(@Valid @RequestBody Vendedor vendedor, BindingResult result, @PathVariable(value="id")Long id ) {
        Proveedor proveedorActual = proveedorService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(proveedorActual == null) {
            response.put("msg","No existe un proveedor con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        //este control creo que no es necesario
        if(vendedorService.findById(vendedor.getId()) == null){
            response.put("msg","No existe un vendedor con id = ".concat(vendedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        Boolean existe = false;
        List<Vendedor> vendedoresActual = proveedorActual.getVendedores();
        for (int i = 0; i < vendedoresActual.size(); i++) {
            if (vendedoresActual.get(i).getId() == vendedor.getId()) {
                vendedoresActual.remove(i);
                existe = true;
            }
        }
        if(!existe){
            response.put("msg","El proveedor no tiene un vendedor con id = ".concat(vendedor.getId().toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        proveedorActual.setVendedores(vendedoresActual);
        try {
            proveedorActual = proveedorService.save(proveedorActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar modificar el proveedor");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Vendedor quitado correctamente");
        response.put("proveedor", proveedorActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("buscar/{query}")
    public ResponseEntity<?> buscarProveedor (@RequestParam(value = "query",required = false) String query){
        Map<String, Object> response = new HashMap<>();
        List<Proveedor> proveedores = new ArrayList<>();
        if(query == null || query.isEmpty()){
            query = "";
        }
        try {
            proveedores = proveedorService.buscarProveedores(query.trim());
        } catch (DataAccessException e) {
            List<String> error = new ArrayList<>();
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Proveedor>>(proveedores, HttpStatus.OK);
    }
}
