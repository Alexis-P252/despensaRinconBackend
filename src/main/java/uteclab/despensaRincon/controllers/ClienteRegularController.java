package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.models.services.ClienteRegularService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dr/clienteRegular")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
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
        List<String> error = new ArrayList<>();
            try {
                cr = clienteRegularService.findById(id);
            }catch (DataAccessException e){
                response.put ("msg","Error al acceder a la base de datos");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put ("error",error);
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(cr == null){
                response.put("msg","No existe un Cliente Regular con ese id");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<ClienteRegular>(cr,HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody ClienteRegular clienteR, BindingResult result){
        ClienteRegular newClienteR = null;
        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(clienteRegularService.findByCedula(clienteR.getCedula()) != null){
            error.add("Ya existe un cliente registrado con la cedula " + clienteR.getCedula());
        }

        if(result.hasErrors()){
            for(FieldError err: result.getFieldErrors()){
                error.add("En el campo " + err.getField() + " " +err.getDefaultMessage());
            }
        }

        if(error.size() > 0){
            response.put ("error",error);
            response.put("msg", "Error al validar el Cliente Regular");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }


        try {
            newClienteR = clienteRegularService.save(clienteR);
        }catch (DataAccessException e){
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(newClienteR == null){
            response.put("msg", "Hubo un problema al guardar o modificar el Cliente Regular");
        }
        response.put ("msg", "Cliente Regular creado correctamente");
        response.put("cliente", newClienteR);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){

        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if  (clienteRegularService.findById(id) != null){
            try {
                clienteRegularService.deleteById(id);

            }catch (DataAccessException e){
                response.put("msg", "Error al acceder a la base de datos");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put ("error",error);
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Cliente Regular eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else {
            response.put("msg", "No existe el cliente regular con dicha ID");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ClienteRegular cr, BindingResult result,  @PathVariable(value="id")Long id){

        ClienteRegular actual = clienteRegularService.findById(id);

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(actual == null) {
            response.put("msg","No existe un cliente regular con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("En el campo: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar el cliente regular");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }

        actual.setCedula(cr.getCedula());
        actual.setNombre(cr.getNombre());
        actual.setCelular(cr.getCelular());
        actual.setCorreo(cr.getCorreo());
        actual.setTelefono(cr.getTelefono());

        try {
            actual = clienteRegularService.save(actual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar editar el cliente regular");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Cliente regular  actualizado correctamente");
        response.put("cliente", actual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

}
