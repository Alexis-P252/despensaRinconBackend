package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.RegistroDeuda;
import uteclab.despensaRincon.models.services.RegistroDeudaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dr/registroDeuda")
public class RegistroDeudaController {
    @Autowired
    RegistroDeudaService registroDeudaService;

    @GetMapping("")
    public List<RegistroDeuda> findAll(){
       return registroDeudaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id){

        RegistroDeuda registroDeuda = null;
        Map<String, Object> response = new HashMap<>();

        try {
            registroDeuda = registroDeudaService.findById(id);
        }catch (DataAccessException e){
            response.put("msj", "Error en la BD");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(registroDeuda == null){
            response.put("msj","No se a encontrado un registro de deuda con ese ID");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<RegistroDeuda>(registroDeuda, HttpStatus.OK);
    }

    @PostMapping("")
    public RegistroDeuda save(RegistroDeuda rd){
        return registroDeudaService.save(rd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name="id") Long id){

        Map<String , Object> response = new HashMap<>();

        if (registroDeudaService.findById(id) != null){
            try {
                registroDeudaService.deleteById(id);
            }catch (DataAccessException e){
                response.put("msj","No se a podido eliminar correctamente el Registro de deuda");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("msj" , "Registro de deuda eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
        }
        response.put("msj", "No existe un registro de deuda con dicho ID");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

}
