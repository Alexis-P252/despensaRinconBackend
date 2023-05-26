package uteclab.despensaRincon.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.models.services.CategoriaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/categoria")
@CrossOrigin(origins = "*")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    @GetMapping("")
    public List<Categoria> findAll() {
        return categoriaService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Categoria ca = null;
        Map<String, Object> response = new HashMap<>();
        try {
            ca=categoriaService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ca==null){
            response.put("msg","No existe una categoria con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Categoria>(ca,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Categoria categoria)     {
        Categoria newCategoria =null;
        Map <String,Object> response = new HashMap<>();

        try{
            newCategoria = categoriaService.save(categoria);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar la Categoria");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Categoria creada correctamente");
        response.put("categoria", newCategoria);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult result, @PathVariable(value="id")Long id  ) {

        Categoria categoriaActual = categoriaService.findById(id);

        Map<String,Object> response = new HashMap<>();

        if(categoriaActual == null) {
            response.put("msg","No existe una categoria con id = ".concat(id.toString()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();

            for(FieldError err: result.getFieldErrors()){
                errors.add("En el campo: " + err.getField() + " - " +err.getDefaultMessage());
            }
            response.put("errors", errors);
            response.put("msg", "Error al validar la categoria");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);

        }


        categoriaActual.setNombre(categoria.getNombre());

        try {
            categoriaActual = categoriaService.save(categoriaActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar editar la categoria");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Category actualizada correctamente");
        response.put("categoria", categoriaActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();

        if(categoriaService.findById(id) != null ){
            try{
                categoriaService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar la categoria");
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Categoria eliminada correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("msg","Error tal intentar eliminar la categoria");
            response.put("error", "No existe una categoria con id = " + id);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }



}
