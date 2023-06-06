package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Pedido;
import uteclab.despensaRincon.models.dao.IPedidoDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private IPedidoDao pedidoDao;

    @Override
    public List<Pedido> findAll() {
        return (List<Pedido>) pedidoDao.findAll();
    }

    @Override
    public List<Pedido> findByFinalizadoFalse() {
        return (List<Pedido>) pedidoDao.findByFinalizadoFalse() ;
    }

    @Override
    public Pedido findById(Long id) {
        return pedidoDao.findById(id).orElse(null);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoDao.save(pedido);
    }

    @Override
    public void deleteById(Long id) {
        pedidoDao.deleteById(id);
    }
    @Override
    public List<Pedido>  buscarPedidos (String cedula, String contenido, int filtro){
        if(filtro ==0){
            return pedidoDao.findByCedulaContainingIgnoreCaseAndContenidoContainingIgnoreCase(cedula, contenido);
        }else{
            List<Pedido> pedidos = pedidoDao.findByCedulaContainingIgnoreCaseAndContenidoContainingIgnoreCase(cedula, contenido);
            List<Pedido> pedidosFiltrados = new ArrayList<>();
            if(filtro == 1){
                for (Pedido pedido : pedidos) {
                    if (pedido.getFinalizado()) {
                        pedidosFiltrados.add(pedido);
                    }
                }
            }else{
                for (Pedido pedido : pedidos) {
                    if (!pedido.getFinalizado()) {
                        pedidosFiltrados.add(pedido);
                    }

                }
            }
                return pedidosFiltrados;
        }
    }
}
