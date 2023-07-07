package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.dao.ICompraDao;
import uteclab.despensaRincon.models.dao.IProductoDao;
import uteclab.despensaRincon.models.dao.IProveedorDao;

import java.util.List;

@Service
public class ProveedorService implements IProveedorService{
    @Autowired
    private IProveedorDao proveedorDao;
    @Autowired
    private ICompraService compraService;
    @Autowired
    private IProductoService productoService;
    @Override
    public List<Proveedor> findAll() {
        return (List<Proveedor>) proveedorDao.findAll();
    }
    @Override
    public Proveedor findById(Long id) {
        return proveedorDao.findById(id).orElse(null);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedorDao.save(proveedor);
    }

    @Override
    public List<Proveedor> buscarProveedores (String query) {
        return proveedorDao.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(query,query);
    }

    @Override
    public void deleteById(Long id) {
        Proveedor proveedorDelete = proveedorDao.findById(id).orElse(null);
        if(proveedorDelete == null){
            return;
        }
        proveedorDao.save(proveedorDelete);
        List<Producto> productos = productoService.findByProveedor(id);
        for (Producto producto : productos) {
            List<Proveedor> proveedores = producto.getProveedores();
            for(Proveedor proveedor : proveedores){
                if (proveedor.equals(proveedorDelete)) {
                    proveedores.remove(proveedor);
                    producto.setProveedores(proveedores);
                    break;
                }
            }
            productoService.save(producto);
        }
        List<Compra> compras = compraService.findByProveedor(id);
        for(Compra c : compras){
            c.setProveedor(null);
            compraService.saveCambios(c);
        }
        proveedorDao.deleteById(id);
    }

}
