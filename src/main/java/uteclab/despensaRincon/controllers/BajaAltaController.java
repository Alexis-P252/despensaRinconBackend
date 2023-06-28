package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.annotations.VerificarToken;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.BajaAlta;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.services.BajaAltaService;
import uteclab.despensaRincon.models.services.ProductoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/baja_alta")
public class BajaAltaController {
    @Autowired
    private BajaAltaService bajaAltaService;
    @Autowired
    private ProductoService productoService;
    @VerificarToken
    @GetMapping("")
    public List<BajaAlta> findAll() {
        return bajaAltaService.findAll();
    }
    @VerificarToken
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        BajaAlta ba = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try {
            ba=bajaAltaService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ba==null){
            response.put("msg","No existe una Baja o Alta con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BajaAlta>(ba,HttpStatus.OK);
    }
    @VerificarToken
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody BajaAlta bajaAlta, BindingResult result)     {
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(result.hasErrors()){
            for(FieldError err: result.getFieldErrors())
                error.add("En el campo " + err.getField() + " " + err.getDefaultMessage());
            response.put("error", error);
            response.put("msg", "Error al validar la Baja o Alta");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        if(bajaAlta.getProducto().getId()==null || productoService.findById(bajaAlta.getProducto().getId()) ==null){
            response.put("error","No se encontro producto con ese Id");
            response.put("msg", "Error al validar la Baja o Alta");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        Producto producto = productoService.findById(bajaAlta.getProducto().getId());
        bajaAlta.setProducto(producto);
        if(bajaAlta.getAlta()) {
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() + bajaAlta.getCantidad());
        }else{
            if(bajaAlta.getCantidad() > bajaAlta.getProducto().getStock()){
                response.put("error","No hay suficiente stock para realizar la baja");
                response.put("msg", "Error al validar la Baja o Alta");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() - bajaAlta.getCantidad());
        }
        try {
            bajaAltaService.save(bajaAlta);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg","Baja o Alta creada con exito");
        response.put("baja_alta",bajaAlta);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }
    @VerificarToken
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {
        BajaAlta bajaAlta = bajaAltaService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        String queEs ="";
        if(bajaAlta==null){
            response.put("msg","No existe baja o alta con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        if(bajaAlta.getAlta()){
            if((bajaAlta.getProducto().getStock() - bajaAlta.getCantidad()) < 0){
                response.put("msg","No se puede eliminar la alta, no hay suficiente stock para retirarlo");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
            queEs="alta";
        }else{
            queEs="baja";
        }
        try{
            bajaAltaService.deleteByObject (bajaAlta);
        }catch(DataAccessException e){
            response.put("msg","Hubo un error al intentar eliminar la "+queEs+"");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("msg","La "+queEs+" eliminada correctamente");
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }

}
