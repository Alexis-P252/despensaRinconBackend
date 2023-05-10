package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.dao.IProveedorDao;

import java.util.List;

@Service
public class ProveedorService implements IProveedorService{
    @Autowired
    private IProveedorDao proveedorDao;
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
    public void deleteById(Long id) {
        proveedorDao.deleteById(id);
    }
 //   @Override
   // public void putVendedor(Vendedor vendedor){ }


}
