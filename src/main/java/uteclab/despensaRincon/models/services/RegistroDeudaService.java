package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.RegistroDeuda;
import uteclab.despensaRincon.models.dao.IRegistroDeudaDao;

import java.util.List;
@Service
public class RegistroDeudaService implements IRegistroDeudaService{
     @Autowired
     public IRegistroDeudaDao registroDeudaDao;
    @Override
    public List<RegistroDeuda> findAll() {
        return (List<RegistroDeuda>) registroDeudaDao.findAll();
    }
    @Override
    public RegistroDeuda findById(Long id) {
        return registroDeudaDao.findById(id).orElse(null);
    }
    @Override
    public RegistroDeuda save(RegistroDeuda em) {
        return  registroDeudaDao.save(em);
    }
    @Override
    public void deleteById(Long id) {
        registroDeudaDao.deleteById(id);
    }
}