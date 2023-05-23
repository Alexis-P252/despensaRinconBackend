package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.BajaAlta;
import uteclab.despensaRincon.models.dao.IBajaAltaDao;

import java.util.List;
import java.util.Map;

@Service
public class BajaAltaService implements IBajaAltaService{
    @Autowired
    public IBajaAltaDao bajaAltaDao;
    @Autowired
    public IProductoService productoService;
    @Override
    public List<BajaAlta> findAll() {
        return (List<BajaAlta>) bajaAltaDao.findAll();
    }

    @Override
    public BajaAlta findById(Long id) {
        return bajaAltaDao.findById(id).orElse(null);
    }

    @Override
    public BajaAlta save(BajaAlta bajaAlta) {
        return bajaAltaDao.save(bajaAlta);
    }

    @Override
    public void deleteByObject (BajaAlta bajaAlta) {
        if (bajaAlta.getAlta()) {
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() - bajaAlta.getCantidad());
        }else{
            bajaAlta.getProducto().setStock(bajaAlta.getProducto().getStock() + bajaAlta.getCantidad());
        }
        productoService.save(bajaAlta.getProducto());
        bajaAlta.setProducto(null);
        bajaAltaDao.save(bajaAlta);
        bajaAltaDao.deleteById(bajaAlta.getId());
    }
}
