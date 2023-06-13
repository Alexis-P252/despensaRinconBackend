package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.dao.IEstadisticaDao;

import java.util.Date;
import java.util.List;
@Service
public class EstadisticaService {

    @Autowired
    private IEstadisticaDao  estadisticaDao;

    public List<Estadistica>  cantProductosPorCategoria(){
        return estadisticaDao.cantProductosPorCategoria();
    }
    public List<Estadistica>  cantVentasPorCategoria(Date fechaInicial, Date fechaFinal){
      //  return estadisticaDao.cantVentasPorCategoria(fechaInicial,fechaFinal);
        return null;
    }

    public Estadistica ganancia (Date fechaInicial, Date fechaFinal){
        Estadistica estadistica = estadisticaDao.ganancias(fechaInicial,fechaFinal);
        estadistica.setCant1(estadistica.getCant2()-estadistica.getCant3());
        return estadistica;
    }

}
