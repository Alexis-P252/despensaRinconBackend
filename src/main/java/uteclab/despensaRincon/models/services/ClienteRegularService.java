package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.entities.Venta;
import uteclab.despensaRincon.models.dao.IClienteRegularDao;

import java.util.List;

@Service
public class ClienteRegularService implements IClienteRegularService{
    @Autowired
    private IClienteRegularDao clienteRegularDao;

    @Autowired
    private VentaService ventaService;

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
    public void deleteById(Long id) {

        List<Venta> ventas = ventaService.findAll();

        for (Venta v: ventas) {
            if(v.getCliente() != null && v.getCliente().getId() == id ){
                v.setCliente(null);
                try{
                    ventaService.save(v);
                }catch(Exception e){

                }
            }
        }
        clienteRegularDao.deleteById(id); }
    @Override
    public List<String> findAllCorreos(){ return clienteRegularDao.findAllCorreos();}
    @Override
    public List<ClienteRegular> buscarClientesRegulares(String cedula, String nombre, String correo) {
        return clienteRegularDao.findByCedulaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(cedula,nombre,correo);
    }
}
