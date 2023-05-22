package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.Compra;

import java.util.List;

public interface ICompraService {
    public List<Compra> findAll();
    public  Compra findById(Long id);
    public Compra save (Compra compra);
    public void deleteByObject (Compra compra);

}
