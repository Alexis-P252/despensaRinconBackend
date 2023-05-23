package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Venta;
import uteclab.despensaRincon.exceptions.StockInsuficienteException;

import java.util.List;

public interface IVentaService {

    public List<Venta> findAll();

    public Venta findById(Long id);

    public Venta save(Venta venta) throws StockInsuficienteException;

    public void deleteById(Long id);
}
