package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Categoria;

import java.util.List;

public interface ICategoriaService {
    public List<Categoria> findAll();
    //// aca tengo una duda
    public Categoria findById(Long id);
    public Categoria save (Categoria em);
    public void deleteById (Long id);
}
