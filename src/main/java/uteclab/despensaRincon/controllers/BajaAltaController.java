package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.BajaAlta;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.services.BajaAltaService;
import uteclab.despensaRincon.models.services.ProductoService;

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
    @GetMapping("")
    public List<BajaAlta> findAll() {
        return bajaAltaService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        BajaAlta ba = null;
        Map<String, Object> response = new HashMap<>();
        try {
            ba=bajaAltaService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ba==null){
            response.put("msg","No existe una Baja o Alta con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BajaAlta>(ba,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody BajaAlta bajaAlta)     {
        Map<String, Object> response = new HashMap<>();
        Producto producto = productoService.findById(bajaAlta.getProducto().getId());
        if(bajaAlta.getCantidad()<=0){
            response.put("msg","Se debe ingresar un valor mayor a 0");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        if(producto==null){
            response.put("msg","No se encontró Producto con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        bajaAlta.setProducto(producto);
        if(bajaAlta.getAlta()) {
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() + bajaAlta.getCantidad());
        }else{
            if(bajaAlta.getCantidad() > bajaAlta.getProducto().getStock()){
                response.put("msg","No hay suficiente stock para realizar la baja");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() - bajaAlta.getCantidad());
        }
        try {
            bajaAltaService.save(bajaAlta);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg","Baja o Alta creada con exito");
        response.put("baja_alta",bajaAlta);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {
        BajaAlta bajaAlta = bajaAltaService.findById(id);
        Map<String,Object> response = new HashMap<>();
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
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("msg","La "+queEs+" eliminada correctamente");
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }





}
