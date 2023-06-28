/*
package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.RegistroDeuda;
import uteclab.despensaRincon.models.services.RegistroDeudaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dr/registroDeuda")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
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
        List<String> error = new ArrayList<>();

        try {
            registroDeuda = registroDeudaService.findById(id);
        }catch (DataAccessException e){
            response.put("msg", "Error en la BD");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(registroDeuda == null){
            response.put("msg","No se a encontrado un registro de deuda con ese ID");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<RegistroDeuda>(registroDeuda, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity save(@RequestBody RegistroDeuda rd){

        RegistroDeuda newRegistro =null;
        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try{
            newRegistro = registroDeudaService.save(rd);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar el registro de deuda");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Registro creado correctamente");
        response.put("registro", newRegistro);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name="id") Long id){

        Map<String , Object> response = new HashMap<>();

        if (registroDeudaService.findById(id) != null){
            try {
                registroDeudaService.deleteById(id);
            }catch (DataAccessException e){
                response.put("msg","No se ha podido eliminar correctamente el Registro de deuda");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.put("msg" , "Registro de deuda eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
        }
        response.put("msg", "No existe un registro de deuda con dicho ID");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

}
*/