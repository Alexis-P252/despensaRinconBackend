package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.models.dao.IAvisoDao;

import java.util.List;
@Service
public class AvisoService implements IAvisoService{

    @Autowired
    private IAvisoDao avisoDao;

    @Override
    public List<Aviso> findAll() {
        return (List<Aviso>) avisoDao.findAll();
    }

    @Override
    public Aviso findById(Long id) {
        return avisoDao.findById(id).orElse(null);
    }

    @Override
    public Aviso save(Aviso aviso) {
        return avisoDao.save(aviso);
    }

    @Override
    public void deleteById(Long id) {
        avisoDao.deleteById(id);
    }
}
