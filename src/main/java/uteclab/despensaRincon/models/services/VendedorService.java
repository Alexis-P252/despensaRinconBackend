package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Vendedor;
import uteclab.despensaRincon.models.dao.IVendedorDao;

import java.util.List;
@Service
public class VendedorService implements IVendedorService{

    @Autowired
    private IVendedorDao vendedorDao;
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
        vendedorDao.deleteById(id);
    }
}
