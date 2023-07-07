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
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.LineaCompra;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.models.services.CompraService;
import uteclab.despensaRincon.models.services.ProductoService;
import uteclab.despensaRincon.models.services.ProveedorService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/compra")
@CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
public class CompraController {

    @Autowired
    private CompraService compraService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoService productoService;
    @VerificarToken
    @GetMapping("")
    public List<Compra> findAll() {
        return compraService.findAll();
    }
    @VerificarToken
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Compra compra = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        try {
            compra = compraService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (compra==null){
            response.put("msg","No existe un compra con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Compra>(compra,HttpStatus.OK);
    }
    @VerificarToken
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Compra compra, BindingResult result){
        Compra compraNew = null;
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();

        Proveedor proveedor =null;
        if (compra.getProveedor()==null){
            error.add("No se selecciono un proveedor");
        }else{
            proveedor = proveedorService.findById(compra.getProveedor().getId());
            if (proveedor==null){
                    error.add("No existe un proveedor con ese id");
            }
        }
        if(result.hasErrors()){
            for(FieldError err: result.getFieldErrors())
                error.add("En el campo " + err.getField() + " " + err.getDefaultMessage());
        }
        if (compra.getLineasCompra().size()==0){
            error.add("No tiene lineas de compra");
        }else{
            for (int i=0;i<compra.getLineasCompra().size();i++){
                if (compra.getLineasCompra().get(i).getProducto()==null){
                    error.add("No se selecciono un producto");
                }else{
                    if (compra.getLineasCompra().get(i).getProducto().getId()==null || compra.getLineasCompra().get(i).getProducto().getId()==0){
                        error.add("No se selecciono producto en al menos una linea");
                    }else{
                        Producto producto =productoService.findById(compra.getLineasCompra().get(i).getProducto().getId());
                            if (producto==null) {
                                error.add("No existe un producto con ese id");
                            }else{
                                compra.getLineasCompra().get(i).setProducto(producto);
                            }
                            if(compra.getLineasCompra().get(i).getCantidad()<=0){
                                error.add("Cantidad no puede ser igual o menor 0");
                            }
                            if(compra.getLineasCompra().get(i).getPrecio()<=0){
                                error.add("Precio compra no puede ser igual o menor a 0");
                            }

                        }
                    }
                }
            }

        if(!error.isEmpty()){
            response.put("error",error);
            response.put("msg","Error al validar la Compra");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        compra.setProveedor(proveedor);
        try {
            compraNew = compraService.save(compra);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put ("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg","Compra creada con exito");
        response.put("compra",compraNew);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }
    @VerificarToken
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {
        Map<String, Object> response = new HashMap<>();
        List<String> error = new ArrayList<>();
        Compra compra = compraService.findById(id);
        if(compra ==null){

            response.put("msg","Hubo un error al intentar eliminar el compra");
            error.add("No existe una compra con ese id");
            response.put("error",error);
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (compra.getLineasCompra().size()>0) {
            for (LineaCompra lc : compra.getLineasCompra()) {
                Producto producto = productoService.findById(lc.getProducto().getId());
                if (producto != null) {
                    if ((producto.getStock() - lc.getCantidad()) < 0) {
                        response.put("msg", "Hubo un error al intentar eliminar el compra");
                        error.add("No se puede eliminar el producto".concat(lc.getProducto().getNombre()).concat(" ya que su stock queda negativo"));
                        response.put("error", error);
                        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                    } else {
                        producto.setStock(producto.getStock() - lc.getCantidad());
                        lc.setProducto(producto);
                    }
                }
            }
        }
            try {
                compraService.deleteByObject(compra);
            }catch(DataAccessException e){
                response.put("msg","Hubo un error al intentar eliminar el compra");
                error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                response.put("error", error);
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Compra eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }
}
