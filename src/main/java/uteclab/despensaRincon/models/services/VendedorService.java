package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.dao.IVendedorDao;

import java.util.ArrayList;
import java.util.List;
@Service
public class VendedorService implements IVendedorService {

    @Autowired
    private IVendedorDao vendedorDao;
    @Autowired
    private IProveedorService proveedorService;

    @Override
    public List<Vendedor> findAll() {
        return (List<Vendedor>) vendedorDao.findAll();
    }

    @Override
    public Vendedor findById(Long id) {
        return vendedorDao.findById(id).orElse(null);
    }

    @Override
    public Vendedor save(Vendedor vr) {
        return vendedorDao.save(vr);
    }

    @Override
    public void deleteById(Long id) {
        List<Proveedor> proveedores= vendedorDao.findAllByVendedorId(id);
        for (Proveedor proveedor : proveedores) {
            proveedor.getVendedores().removeIf(vendedor -> vendedor.getId().equals(id));
            proveedorService.save(proveedor);
        }
        vendedorDao.deleteById(id);
    }
    public List<Vendedor> buscarVendedores(String query) {
        return vendedorDao.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(query, query);
    }

   /* @Override
    public List<Vendedor> buscarVendedores(String query, Long proveedor) {
        List<Vendedor> vendedores = new ArrayList<>();
        if (proveedor == 0) {
            return vendedorDao.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(query, query);
        } else {
            List<Vendedor> todosLosVendedores = vendedorDao.findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(query, query);
            for (Vendedor vendedor : todosLosVendedores) {
                List<Proveedor> proveedores = vendedor.getProveedores();
                for (Proveedor pr : proveedores)
                    if (pr.getId() == proveedor) {
                        vendedores.add(vendedor);
                        break;
                    }
            }
            return vendedores;
        }

    }

    */
}
