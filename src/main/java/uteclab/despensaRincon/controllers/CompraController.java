package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.LineaCompra;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.models.services.CompraService;
import uteclab.despensaRincon.models.services.ProductoService;
import uteclab.despensaRincon.models.services.ProveedorService;


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
    
    @GetMapping("")
    public List<Compra> findAll() {
        return compraService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") Long id){
        Compra compra = null;
        Map<String, Object> response = new HashMap<>();
        try {
            compra = compraService.findById(id);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (compra==null){
            response.put("msg","No existe un compra con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Compra>(compra,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Compra compra){
        Compra compraNew = null;
        Map<String, Object> response = new HashMap<>();
        Proveedor proveedor =null;
        if (compra.getProveedor()==null){
            response.put("msg","No se selecciono un proveedor");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }else{
            proveedor = proveedorService.findById(compra.getProveedor().getId());
            if (proveedor==null){
                    response.put("msg","No existe un proveedor con ese id");
                    return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
            }
        }

        if (compra.getLineasCompra().size()==0){
            response.put("msg","No tiene lineas de compra");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }else{
            for (int i=0;i<compra.getLineasCompra().size();i++){
                if (compra.getLineasCompra().get(i).getProducto()==null){
                    response.put("msg","No se selecciono un producto");
                    return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
                }else{
                    if (compra.getLineasCompra().get(i).getProducto().getId()==null || compra.getLineasCompra().get(i).getProducto().getId()==0){
                        response.put("msg","No se selecciono un producto");
                        return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
                    }else{
                        Producto producto =productoService.findById(compra.getLineasCompra().get(i).getProducto().getId());
                            if (producto==null) {
                                response.put("msg", "No existe un producto con ese id");
                                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                            }
                            if(compra.getLineasCompra().get(i).getCantidad()<=0){
                                response.put("msg", "Cantidad no puede ser 0");
                                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                            }
                            if(compra.getLineasCompra().get(i).getPrecio()<=0){
                                response.put("msg", "Precio no puede ser 0");
                                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                            }
                        compra.getLineasCompra().get(i).setProducto(producto);
                        }
                    }
                }
            }
        compra.setProveedor(proveedor);
        try {
            compraNew = compraService.save(compra);
        }catch (DataAccessException e){
            response.put ("msg","Error al acceder a la base de datos");
            response.put ("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("msg","Compra creada con exito");
        response.put("compra",compraNew);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Compra compra = compraService.findById(id);
        if(compra ==null){

            response.put("msg","Hubo un error al intentar eliminar el compra");
            response.put("error","No existe compra con ese id");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (compra.getLineasCompra().size()>0) {
            for (LineaCompra lc : compra.getLineasCompra()) {
                Producto producto = productoService.findById(lc.getProducto().getId());
                if (producto != null) {
                    if ((producto.getStock() - lc.getCantidad()) < 0) {
                        response.put("msg", "Hubo un error al intentar eliminar el compra");
                        response.put("error", "No se puede eliminar producto el " + lc.getProducto().getNombre() + " queda en menor a 0");
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
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("msg","Aviso eliminado correctamente");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);

    }


}
