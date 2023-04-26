package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Aviso;

import java.util.List;

public interface IAvisoService {

    public List<Aviso> findAll();

    public Aviso findById(Long id);
    public Aviso save (Aviso aviso);
    public void deleteById (Long id);
}
