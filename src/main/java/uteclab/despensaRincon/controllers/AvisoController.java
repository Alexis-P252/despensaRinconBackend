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
import uteclab.despensaRincon.models.services.EmailService;

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
    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public List<Aviso> findAll() {
        return avisoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id") Long id) {
        Aviso aviso = null;
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();
        try {
            aviso = avisoService.findById(id);
        } catch (DataAccessException e) {
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (aviso == null) {
            response.put("msg", "No existe un aviso con ese id");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Aviso>(aviso, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Aviso aviso, BindingResult result) {
        Aviso newAviso = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if (result.hasErrors()) {
            for (FieldError err : result.getFieldErrors()) {
                error.add("En el campo " + err.getField() + " " + err.getDefaultMessage());
            }
            response.put("error", error);
            response.put("msg", "Error al validar el aviso");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        String respuesta = "";
        try {
            newAviso = avisoService.save(aviso);
            respuesta = emailService.mandarEmailHtml(aviso.getTitulo(), aviso.getDescripcion());
        } catch (DataAccessException e) {
            response.put("msg", "Error al intentar actualizar o ingresar el aviso");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Aviso creado correctamente. " + respuesta);
        response.put("aviso", newAviso);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Aviso aviso, BindingResult result, @PathVariable(value = "id") Long id) {

        Aviso avisoActual = avisoService.findById(id);

        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if (avisoActual == null) {
            response.put("error", "No existe un aviso con id = ".concat(id.toString()));
            response.put("msg", "Error al encontrar el aviso");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();

            for (FieldError err : result.getFieldErrors()) {
                errors.add("En el campo " + err.getField() + " " + err.getDefaultMessage());
            }
            response.put("error", errors);
            response.put("msg", "Error al validar el aviso");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

        }

        avisoActual.setTitulo(aviso.getTitulo());
        avisoActual.setDescripcion(aviso.getDescripcion());
        String respuesta = "";
        try {
            avisoActual = avisoService.save(avisoActual);
            respuesta = emailService.mandarEmailHtml(aviso.getTitulo(), aviso.getDescripcion());

        } catch (DataAccessException e) {
            response.put("msg", "Error al intentar editar el aviso");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Aviso actualizado correctamente. " + respuesta);
        response.put("aviso", avisoActual);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();


        if (avisoService.findById(id) != null) {
            try {
                avisoService.deleteById(id);

            } catch (DataAccessException e) {
                response.put("msg", "Hubo un error al intentar eliminar el aviso");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg", "Aviso eliminado correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

        } else {
            response.put("msg", "Error al intentar eliminar el aviso");
            error.add("No existe un aviso con id = " + id);
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("reenvio/{id}")
    public ResponseEntity<?> reenvioAviso (@PathVariable(value = "id") Long id){
        Aviso aviso = null;
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();
        try {
            aviso = avisoService.findById(id);
        } catch (DataAccessException e) {
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (aviso == null) {
            response.put("msg", "No existe un aviso con ese id");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        String respuesta = "";
        try {
            respuesta = emailService.mandarEmailHtml(aviso.getTitulo(), aviso.getDescripcion());
        } catch (DataAccessException e) {
            response.put("msg", "Error al enviar correo");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Reenvio realizado " + respuesta);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    //busca por titulo y por descripcion
    @GetMapping("buscar/{query}")
    public ResponseEntity<?> buscarAviso (@RequestParam(value = "query",required = false) String query){
        Map<String, Object> response = new HashMap<>();
        List<Aviso> avisos = new ArrayList<>();
        if(query == null || query.isEmpty()){
            query = "";
        }
        try {
            avisos = avisoService.buscarAvisos(query,query);
        } catch (DataAccessException e) {
            List<String> error = new ArrayList<>();
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Aviso>>(avisos, HttpStatus.OK);
    }
}
