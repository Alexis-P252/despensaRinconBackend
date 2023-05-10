package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.models.services.ClienteRegularService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dr/clienteRegular")
public class ClienteRegularController {

    @Autowired
    ClienteRegularService clienteRegularService;

    @GetMapping("")
    public List<ClienteRegular> findAll(){
        return  clienteRegularService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        ClienteRegular cr = null;
        Map<String, Object> response = new HashMap<>();
            try {
                cr = clienteRegularService.findById(id);
            }catch (DataAccessException e){
                response.put ("msg","Error al acceder a la base de datos");
                response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(cr == null){
                response.put("msg","No existe un Cliente Regular con ese id");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<ClienteRegular>(cr,HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ClienteRegular clienteR){
        ClienteRegular newClienteR = null;
        Map <String,Object> response = new HashMap<>();

        try {
            newClienteR = clienteRegularService.save(clienteR);
        }catch (DataAccessException e){
            response.put("msg", "Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(newClienteR == null){
            response.put("msj", "Hubo un problema al guardar o modificar el Cliente Regular");
        }
        response.put ("msg", "Cliente Regular creado correctamente");
        response.put("categoria", newClienteR);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){

        Map <String,Object> response = new HashMap<>();

        if  (clienteRegularService.findById(id) != null){
            try {
                clienteRegularService.deleteById(id);

            }catch (DataAccessException e){
                response.put("msg", "Error al acceder a la base de datos");
                response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msj","Cliente Regular eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else {
            response.put("msj", "No existe el cliente regular con dicha ID");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody ClienteRegular cr){

        Map <String,Object> response = new HashMap<>();

        if (clienteRegularService.findById(cr.getId()) != null) {
            try {
                clienteRegularService.update(cr);
            }catch (DataAccessException e){
                response.put("msg", "Error al acceder a la base de datos");
                response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("msj", "Cliente regular actualizado correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }else{
            response.put("msj" , "No existe el cliente regular con dicha id");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
