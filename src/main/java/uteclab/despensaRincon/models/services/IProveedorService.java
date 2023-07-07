package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;

import java.util.List;

public interface IProveedorService {
    public List<Proveedor> findAll();

    public Proveedor findById(Long id);
    public Proveedor save (Proveedor proveedor);
    public void deleteById (Long id);
    public List<Proveedor> buscarProveedores (String query);
    //public void putVendedor (Vendedor vendedor);
}
