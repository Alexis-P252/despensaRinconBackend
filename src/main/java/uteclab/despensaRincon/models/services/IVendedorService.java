package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Vendedor;

import java.util.List;

public interface IVendedorService {
    public List<Vendedor> findAll();

    public Vendedor findById(Long id);
    public Vendedor save (Vendedor vendedor);
    public void deleteById (Long id);
}
