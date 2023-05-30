package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.models.dao.IClienteRegularDao;

import java.util.List;

@Service
public class ClienteRegularService implements IClienteRegularService{
    @Autowired
    private IClienteRegularDao clienteRegularDao;
    @Override
    public List<ClienteRegular> findAll() {return (List<ClienteRegular>) clienteRegularDao.findAll();}
    @Override
    public ClienteRegular findById(Long id) { return clienteRegularDao.findById(id).orElse(null); }

    @Override
    public ClienteRegular findByCedula(String cedula) {
        return clienteRegularDao.findByCedula(cedula).orElse(null);
    }

    @Override
    public ClienteRegular save(ClienteRegular cr) { return clienteRegularDao.save(cr); }
    @Override
    public void deleteById(Long id) { clienteRegularDao.deleteById(id); }
}
