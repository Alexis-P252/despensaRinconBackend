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
    @VerificarToken
    @GetMapping("")
    public List<Categoria> findAll() {
        return categoriaService.findAll();
    }
    @VerificarToken
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Categoria ca = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try {
            ca=categoriaService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (ca==null){
            response.put("msg","No existe una categoria con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Categoria>(ca,HttpStatus.OK);
    }
    @VerificarToken
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Categoria categoria, BindingResult result)     {
        Categoria newCategoria =null;
        Map <String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(null != categoriaService.findByNombre(categoria.getNombre())){
            error.add("Ya existe una categoria con ese nombre");
        }
        if(result.hasErrors()){
            for(FieldError err: result.getFieldErrors()){
                error.add("En el campo " + err.getField() + " " +err.getDefaultMessage());
            }
        }
        if(!error.isEmpty()){
            response.put("error", error);
            response.put("msg", "Error al validar la Categoria");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            newCategoria = categoriaService.save(categoria);
        } catch (DataAccessException e){
            response.put("msg","Error al intentar actualizar o ingresar la Categoria");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put ("msg", "Categoria creada correctamente");
        response.put("categoria", newCategoria);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    @VerificarToken
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult result, @PathVariable(value="id")Long id  ) {

        Categoria categoriaActual = categoriaService.findById(id);

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        if(categoriaActual == null) {
            response.put("error","No existe una categoria con id = ".concat(id.toString()));
            response.put("msg", "Error al encontrar la categoria");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        if(null != categoriaService.findByNombre(categoria.getNombre())){
            error.add("Ya existe una categoria con ese nombre");
        }
        if(result.hasErrors()){
            for(FieldError err: result.getFieldErrors()){
                error.add("En el campo " + err.getField() + " " +err.getDefaultMessage());
            }
        }
        if(!error.isEmpty()){
            response.put("error", error);
            response.put("msg", "Error al validar la Categoria");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        categoriaActual.setNombre(categoria.getNombre());
        try {
            categoriaActual = categoriaService.save(categoriaActual);
        }catch(DataAccessException e) {
            response.put("msg","Error al intentar editar la categoria");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg", "Categoria actualizada correctamente");
        response.put("categoria", categoriaActual);
        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
    }
    @VerificarToken
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {

        Map<String,Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        if(categoriaService.findById(id) != null ){
            try{
                categoriaService.deleteById(id);

            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar la categoria");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Categoria eliminada correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

        }else{
            response.put("error","No se encontro la categoria "+id);
            response.put("msg","Error tal intentar eliminar la categoria");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @VerificarToken
    @GetMapping("buscar/{query}")
    public ResponseEntity<?> buscarCategoria (@RequestParam(value = "query",required = false) String query){
        Map<String, Object> response = new HashMap<>();
        List<Categoria> categorias = new ArrayList<>();
        if(query == null || query.isEmpty()){
            query = "";
        }
        try {
            categorias = categoriaService.buscarCategorias(query.trim());
        } catch (DataAccessException e) {
            List<String> error = new ArrayList<>();
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Categoria>>(categorias, HttpStatus.OK);
    }

}
