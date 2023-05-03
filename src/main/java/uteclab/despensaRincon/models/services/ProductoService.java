package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Producto;
import uteclab.despensaRincon.models.dao.IProductoDao;

import java.util.List;
@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoDao productoDao;
    @Override
    public List<Producto> findAll() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    public Producto findByNombre(String nombre) {
        return productoDao.findByNombre(nombre);
    }

    @Override
    public Producto save(Producto pr) {
        return productoDao.save(pr);
    }

    @Override
    public void deleteById(Long id) {
            productoDao.deleteById(id);
    }

}
