package uteclab.despensaRincon.models.dao;

import org.apache.logging.log4j.spi.ObjectThreadContextMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.entities.Producto;

import java.util.Date;
import java.util.List;

public interface IEstadisticaDao extends CrudRepository<Producto, Long> {

    @Query("SELECT new uteclab.despensaRincon.entities.Estadistica(c.nombre, COUNT(p.id)) FROM Categoria c JOIN c.productos p GROUP BY c.nombre")
    List<Estadistica> cantProductosPorCategoria();
/*
    @Query(value = "SELECT c.nombre AS nombre, CAST(SUM(l.cantidad) as Float) AS cant1, CAST(SUM(l.precio) AS Float) AS cant2 " +
            "FROM Categoria c " +
            "INNER JOIN Producto p ON c.id = p.categoria_id " +
            "INNER JOIN Linea_Venta l ON p.id = l.producto_id " +
            "INNER JOIN venta_lineas_venta vlv ON l.id = vlv.lineas_venta_id " +
            "INNER JOIN Venta v ON vlv.venta_id = v.id " +
            "GROUP BY c.nombre", nativeQuery = true)
 */

    @Query(value = "SELECT c.nombre, SUM(l.cantidad), SUM(l.precio) " +
            "FROM Categoria c " +
            "INNER JOIN Producto p ON c.id = p.categoria_id " +
            "INNER JOIN Linea_Venta l ON p.id = l.producto_id " +
            "INNER JOIN venta_lineas_venta vlv ON l.id = vlv.lineas_venta_id " +
            "INNER JOIN Venta v ON vlv.venta_id = v.id " +
            "WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal " +
            "GROUP BY c.nombre", nativeQuery = true)
    List<Object[]> getCantidadVentasPorCategoria (@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

   @Query("SELECT  new uteclab.despensaRincon.entities.Estadistica('' AS nombre, CAST (0 as Float),   " +
           "(SELECT CAST(COALESCE(SUM(v.total), 0) as Float) FROM Venta v WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal) AS cant1, " +
           "(SELECT CAST(COALESCE(SUM(c.total), 0) as Float) FROM Compra c WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal)) AS cant2 ")
   Estadistica ganancias(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
}
