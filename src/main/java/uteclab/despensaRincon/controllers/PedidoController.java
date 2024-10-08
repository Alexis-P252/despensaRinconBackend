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
import uteclab.despensaRincon.annotations.VerificarToken;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.entities.Pedido;
import uteclab.despensaRincon.models.services.ClienteRegularService;
import uteclab.despensaRincon.models.services.PedidoService;
import com.fabdelgado.ciuy.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/pedido")
@CrossOrigin(origins = "*")
public class PedidoController {


    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteRegularService clienteRegularService;

    @GetMapping("")
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/pendientes")
    public List<Pedido> findPendientes(){
        return pedidoService.findByFinalizadoFalse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Pedido pedido = null;
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();

        try {
            pedido = pedidoService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (pedido==null){
            response.put("msg","No existe un pedido con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Pedido>(pedido,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody Pedido pedido, BindingResult result){

        Pedido newPedido = null;
        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("En el campo: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar el pedido");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        String cedula = pedido.getCedula();

        ClienteRegular c = clienteRegularService.findByCedula(cedula);

        if( c == null ){
            response.put("msg", "Hubo un error al realizar el pedido");
            error.add("La cedula especificada no pertenece a ningun cliente registrado");
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        else{
            pedido.setCliente(c);
            pedido.setFinalizado(false);

            try{
                pedidoService.save(pedido);
            }catch (DataAccessException e ){
                response.put ("msg","Error al acceder a la base de datos");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put ("error",error);
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg", "Pedido realizado correctamente!");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
        }
    }

    @PutMapping("/finalizado/{id}")
    public ResponseEntity<?> marcarFinalizado(@PathVariable(value="id") Long id ){

        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        Pedido pedido = pedidoService.findById(id);

        if(pedido == null){
            response.put("msg","Hubo un error");
            error.add("No existe un pedido con id = " + id);
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

        pedido.setFinalizado(true);

        try{
            pedidoService.save(pedido);
        }catch (DataAccessException e ){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("msg", "Pedido marcado como finalizado correctamente!");
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
    }
    @VerificarToken
    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminar(@PathVariable(value="id") Long id ){

        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(pedidoService.findById(id) != null ){
            try{
                pedidoService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar el pedido");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Pedido eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error al intentar eliminar el pedido");
            error.add("No existe un pedido con id = " + id );
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }
    //se puede mandar cedula para buscar por cliente, detalle para buscar dentro del pedido o finalizado, pendiente o todos(para este ultimo debe ser null el finalizado)
    @VerificarToken
    @GetMapping("buscar/{cedula}/{contenido}/{finalizados}")
    public ResponseEntity<?> buscarPedido (@RequestParam(value = "cedula",required = false) String cedula,@RequestParam(value = "contenido",required = false) String contenido, @RequestParam(value = "finalizados",required = false) Boolean finalizado){
        Map<String, Object> response = new HashMap<>();
        List<Pedido> pedidos = new ArrayList<>();
        int filtro =0; // 0 trae todos, 1 trae finalizados, 2 trae pendientes

        if(finalizado == null){
            filtro = 0;
        }else{
            if(finalizado){
                filtro = 1;
            }else{
                filtro =2;
            }
        }
        if(cedula == null){
            cedula="";
        }
        if(contenido == null){
            contenido="";
        }

        Validator validator = new Validator();
        String cedulaFiltrada = validator.cleanCi(cedula);

         try {
             pedidos  = pedidoService.buscarPedidos (cedula.trim(),contenido.trim(),filtro);
        } catch (DataAccessException e) {
            List<String> error = new ArrayList<>();
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Pedido>>(pedidos, HttpStatus.OK);

    }
}
