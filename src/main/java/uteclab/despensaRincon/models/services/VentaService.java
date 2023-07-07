package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.LineaVenta;
import uteclab.despensaRincon.entities.Venta;
import uteclab.despensaRincon.exceptions.StockInsuficienteException;
import uteclab.despensaRincon.models.dao.IVentaDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VentaService implements IVentaService {


    @Autowired
    private IVentaDao ventaDao;


    @Override
    public List<Venta> findAll() {
        return (List<Venta>) ventaDao.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaDao.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) throws StockInsuficienteException {

        Float total = 0F;

        for(LineaVenta lv: venta.getLineasVenta()){

            if(lv.getProducto().getStock() < lv.getCantidad()){
                throw new StockInsuficienteException("No hay suficiente stock como para vender " + lv.getCantidad() + " de " + lv.getProducto().getNombre());
            }

            lv.getProducto().setStock(lv.getProducto().getStock() - lv.getCantidad());
            lv.setPrecio(lv.getCantidad() * lv.getProducto().getPrecio_venta());
            total = total + lv.getPrecio();
        }

        venta.setTotal(total);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(venta.getFecha());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date diaSiguiente = calendar.getTime();
        venta.setFecha(diaSiguiente);

        return ventaDao.save(venta);
    }

    @Override
    public void deleteById(Long id) {
        ventaDao.deleteById(id);
    }
}
