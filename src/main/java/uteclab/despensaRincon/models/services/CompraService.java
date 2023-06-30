package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.LineaCompra;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.models.dao.ICompraDao;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CompraService implements ICompraService {

    @Autowired
    private ICompraDao compraDao;
    @Autowired
    private IProductoService productoService;

    @Override
    public List<Compra> findAll() {
        return (List<Compra>) compraDao.findAll();
    }

    @Override
    public Compra findById(Long id) {
        return compraDao.findById(id).orElse(null);
    }

    @Override
    public Compra save(Compra compra) {
        Float total=0F;
        for(LineaCompra lc: compra.getLineasCompra()){
            lc.setPrecio(lc.getProducto().getPrecio_compra() * lc.getCantidad());
            lc.getProducto().setStock(lc.getProducto().getStock()+lc.getCantidad());
            Boolean existe=false;
            for(Proveedor prv: lc.getProducto().getProveedores()){
                if (prv.getId()==compra.getProveedor().getId()){
                    existe=true;
                }
            }
            if (!existe){
                lc.getProducto().getProveedores().add(compra.getProveedor());
            }
            total= total + lc.getPrecio();
        }
        compra.setTotal(total);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(compra.getFecha());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date diaSiguiente = calendar.getTime();
        compra.setFecha(diaSiguiente);

        return compraDao.save(compra);
    }

    @Override
    public void deleteByObject(Compra compra) {
        for(LineaCompra lc : compra.getLineasCompra()) {
            productoService.save(lc.getProducto());
            lc.setProducto(null);
        }
        compra.setProveedor(null);
        compraDao.save(compra);
        compraDao.deleteById(compra.getId());
    }
    public List<Compra> findByProveedor (Long proveedor_id){
        return compraDao.findByProveedor(proveedor_id);
    }
    @Override
    public Compra saveCambios(Compra compra) {
        return compraDao.save(compra);
    }

}