package uteclab.despensaRincon.controllers;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.LineaCompra;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.entities.Venta;
import uteclab.despensaRincon.exceptions.StockInsuficienteException;
import uteclab.despensaRincon.models.services.IVentaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/venta")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
public class VentaController {


    @Autowired
    private IVentaService ventaService;

    @GetMapping("")
    public List<Venta> findAll(){
        return ventaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id") Long id){

        Venta venta = null;
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try{
            venta = ventaService.findById(id);
        } catch (DataAccessException e){
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(venta == null){
            response.put("msg", "No hay una venta con id " + id.toString());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Venta>(venta, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Venta venta, BindingResult result){

        Venta nuevaVenta = null;
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("En el campo: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar la venta");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            nuevaVenta = ventaService.save(venta);
        }catch(DataAccessException | StockInsuficienteException e ){
            response.put("msg", "Error al intentar insertar la nueva venta");
            error.add(e.getMessage());
            response.put("error", error);

            if(e instanceof  StockInsuficienteException){
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

            }
            else{
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }

        response.put("msg", "Venta creada correctamente");
        response.put("venta", nuevaVenta);
        return new ResponseEntity<Map<String,Object>>( response, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id){
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        Venta venta = ventaService.findById(id);
        if(venta ==null){

            response.put("msg","Hubo un error al intentar eliminar la venta");
            error.add("No existe venta con ese id");
            response.put("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            ventaService.deleteById(id);
        }catch(DataAccessException e){
            response.put("msg","Hubo un error al intentar eliminar la venta");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("msg","Venta eliminada correctamente");
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }




}
