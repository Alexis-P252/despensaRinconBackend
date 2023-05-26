package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.models.services.AvisoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/aviso")
@CrossOrigin(origins = "*")
public class AvisoController {

    @Autowired
    private AvisoService avisoService;
    @GetMapping("")
    public List<Aviso> findAll() {
        return avisoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Aviso aviso = null;
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();
        try {
            aviso = avisoService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (aviso==null){
            response.put("msg","No existe un aviso con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Aviso>(aviso,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Aviso aviso)     {
        Aviso newAviso =null;
        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try{
            newAviso = avisoService.save(aviso);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar el aviso");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Aviso creado correctamente");
        response.put("aviso", newAviso);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Aviso aviso, BindingResult result, @PathVariable(value="id")Long id  ) {

        Aviso avisoActual = avisoService.findById(id);

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(avisoActual == null) {
            response.put("msg","No existe un aviso con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("En el campo: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar el aviso");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }

        avisoActual.setTitulo(aviso.getTitulo());
        avisoActual.setDescripcion(aviso.getDescripcion());

        try {
            avisoActual = avisoService.save(avisoActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar editar el aviso");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Aviso actualizado correctamente");
        response.put("aviso", avisoActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();


        if(avisoService.findById(id) != null ){
            try{
                avisoService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar el aviso");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Aviso eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error al intentar eliminar el aviso");
            error.add("No existe un aviso con id = " + id);
            response.put("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
