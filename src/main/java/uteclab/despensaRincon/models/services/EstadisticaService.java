package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.dao.IEstadisticaDao;

import java.util.List;
@Service
public class EstadisticaService {

    @Autowired
    private IEstadisticaDao  estadisticaDao;

    public List<Estadistica>  cantProductosPorCategoria(){
        return estadisticaDao.cantProductosPorCategoria();
    }

}
