package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.dao.IEstadisticaDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        // Crear un mapa para almacenar los resultados existentes
        Map<String, Float> estadisticasMap = new HashMap<>();
        for (Object[] resultado : resultados) {
            String fechaStr = resultado[0].toString();
            Float total = ((Number) resultado[1]).floatValue();
            estadisticasMap.put(fechaStr, total);
        }

        // Obtener la fecha actual en formato "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = dateFormat.format(new Date());

        // Iterar sobre los últimos 7 días, incluyendo aquellos sin ventas registradas
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            try {
                calendar.setTime(dateFormat.parse(fechaActual));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            String fechaStr = dateFormat.format(calendar.getTime());

            // Verificar si existe un resultado para la fecha actual en el mapa
            if (estadisticasMap.containsKey(fechaStr)) {
                Float total = estadisticasMap.get(fechaStr);

                // Crear un objeto Estadistica y agregarlo a la lista
                Estadistica estadistica = new Estadistica();
                estadistica.setNombre(fechaStr);
                estadistica.setCant1(total);
                estadisticas.add(estadistica);
            } else {
                // Si no hay resultado para la fecha actual, agregar un objeto Estadistica con valor 0
                Estadistica estadistica = new Estadistica();
                estadistica.setNombre(fechaStr);
                estadistica.setCant1(0.0f);
                estadisticas.add(estadistica);
            }
        }

        return estadisticas;
    }
}
