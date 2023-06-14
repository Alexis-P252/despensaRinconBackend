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
///4
    public List<Estadistica>  productosMasVendidos(Date fechaInicial, Date fechaFinal){
        List<Object[]> resultados = estadisticaDao.productosMasVendidos(fechaInicial, fechaFinal);
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
///5
    public List<Estadistica>  productosMasComprados(Date fechaInicial, Date fechaFinal){
        List<Object[]> resultados = estadisticaDao.productosMasComprados(fechaInicial, fechaFinal);
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
///6
    public Estadistica cantVentaClientesRegulares(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal){
        return estadisticaDao.cantVentaClientesRegulares(fechaInicial,fechaFinal);
    }
///7
    public List<Estadistica>  comprasProveedores(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal){
        return estadisticaDao.comprasProveedores(fechaInicial,fechaFinal);
    }
///8
    public List<Estadistica>  mejoresClientes(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal){
        return estadisticaDao.mejoresClientes(fechaInicial,fechaFinal);
    }
///9 ventaUltimos7Dias
    public List<Estadistica>  ventaUltimos7Dias(){
    List<Object[]> resultados = estadisticaDao.ventaUltimos7Dias();
    List<Estadistica> estadisticas = new ArrayList<>();
        for (Object[] resultado : resultados) {
            Estadistica estadistica = new Estadistica();
            estadistica.setNombre( resultado[0].toString());
            estadistica.setCant1(((Number) resultado[1]).floatValue());
            estadisticas.add(estadistica);
        }
        return estadisticas;
    }
}
