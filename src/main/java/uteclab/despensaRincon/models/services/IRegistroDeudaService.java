package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.RegistroDeuda;

import java.util.List;

public interface IRegistroDeudaService  {
    public List<RegistroDeuda> findAll();
    public RegistroDeuda findById(Long id);
    public RegistroDeuda save (RegistroDeuda rd);
    public void deleteById (Long id);


}