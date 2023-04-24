package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.models.services.CategoriaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/categorias")
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
        response.put ("msg", "Datos del Categoria ingresados correctamente");
        response.put("Categoria", newCategoria);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



}
