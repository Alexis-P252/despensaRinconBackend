package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.dao.IEstadisticaDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class EstadisticaService {

    @Autowired
    private IEstadisticaDao  estadisticaDao;
///1
    public List<Estadistica>  cantProductosPorCategoria(){
        return estadisticaDao.cantProductosPorCategoria();
    }
///2
    public List<Estadistica>  cantVentasPorCategoria(Date fechaInicial, Date fechaFinal){
        List<Object[]> resultados = estadisticaDao.getCantidadVentasPorCategoria(fechaInicial, fechaFinal);
        List<Estadistica> estadisticas = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Estadistica estadistica = new Estadistica();
            estadistica.setNombre((String) resultado[0]);
            estadistica.setCant1(((Number) resultado[1]).floatValue());
            estadistica.setCant2(((Number) resultado[2]).floatValue());
            estadisticas.add(estadistica);
        }
        return estadisticas;
    }
///3
    public Estadistica ganancia (Date fechaInicial, Date fechaFinal){
        Estadistica estadistica = estadisticaDao.ganancias(fechaInicial,fechaFinal);
        estadistica.setCant1(estadistica.getCant2()-estadistica.getCant3());
        return estadistica;
    }


}
