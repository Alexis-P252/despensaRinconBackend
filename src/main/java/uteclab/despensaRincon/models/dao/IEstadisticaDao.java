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
///1
    @Query("SELECT new uteclab.despensaRincon.entities.Estadistica(c.nombre, COUNT(p.id)) FROM Categoria c JOIN c.productos p GROUP BY c.nombre")
    List<Estadistica> cantProductosPorCategoria();
///2
    @Query(value = "SELECT c.nombre, SUM(l.cantidad), SUM(l.precio) " +
            "FROM Categoria c " +
            "INNER JOIN Producto p ON c.id = p.categoria_id " +
            "INNER JOIN Linea_Venta l ON p.id = l.producto_id " +
            "INNER JOIN Venta v ON l.venta_id = v.id " +
            "WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal " +
            "GROUP BY c.id", nativeQuery = true)
    List<Object[]> getCantidadVentasPorCategoria (@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///3
   @Query("SELECT  new uteclab.despensaRincon.entities.Estadistica('' AS nombre, CAST (0 as Float),   " +
           "(SELECT CAST(COALESCE(SUM(v.total), 0) as Float) FROM Venta v WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal) AS cant1, " +
           "(SELECT CAST(COALESCE(SUM(c.total), 0) as Float) FROM Compra c WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal)) AS cant2 ")
   Estadistica ganancias(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///4
@Query(value = "SELECT p.nombre, SUM(l.cantidad) as cantidad, SUM(l.precio) " +
        "FROM Producto p " +
        "INNER JOIN Linea_Venta l ON p.id = l.producto_id " +
        "INNER JOIN Venta v ON l.venta_id = v.id  " +
        "WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal " +
        "GROUP BY p.id " +
        "ORDER BY cantidad DESC LIMIT 5 ", nativeQuery = true)
List<Object[]> productosMasVendidos (@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///5
   @Query(value = "SELECT p.nombre, SUM(l.cantidad) as cantidad, SUM(l.precio) " +
           "FROM Producto p " +
           "INNER JOIN Linea_Compra l ON p.id = l.producto_id " +
           "INNER JOIN Compra  c ON l.compra_id = c.id  " +
           "WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal " +
           "GROUP BY p.id " +
           "ORDER BY cantidad DESC LIMIT 5", nativeQuery = true)
    List<Object[]> productosMasComprados (@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///6
    @Query("SELECT  new uteclab.despensaRincon.entities.Estadistica('' AS nombre, " +
            "(SELECT CAST(COALESCE(COUNT(v.id), 0) as Float) FROM Venta v WHERE v.cliente.id IS NOT NULL AND v.fecha BETWEEN :fechaInicial AND :fechaFinal) AS cant1, " +
            "(SELECT CAST(COALESCE(COUNT(ve.id), 0) as Float) FROM Venta ve WHERE ve.cliente.id IS NULL AND ve.fecha BETWEEN :fechaInicial AND :fechaFinal) AS cant2 )")
    Estadistica cantVentaClientesRegulares (@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///7
    @Query("SELECT new uteclab.despensaRincon.entities.Estadistica(p.nombre AS nombre, CAST(COALESCE(COUNT(c.id),0) as Float) AS cant1, CAST(COALESCE(SUM(c.total),0) as Float) AS cant2) FROM Compra c " +
            "JOIN c.proveedor p " +
            "WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal "+
            "GROUP BY p.nombre")
    List<Estadistica> comprasProveedores(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///8
    @Query("SELECT new uteclab.despensaRincon.entities.Estadistica(c.nombre AS nombre, CAST(COALESCE(SUM(v.total), 0) as Float) AS cant1) FROM Venta v " +
            "JOIN v.cliente c " +
            "WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal " +
            "GROUP BY nombre " +
            "ORDER BY cant1 DESC LIMIT 5")
    List<Estadistica> mejoresClientes(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
///9
    @Query(value = "SELECT DATE(fecha) AS fecha, SUM(total) AS total "+
    "FROM venta "+
    "WHERE fecha >= (SELECT CURRENT_DATE - INTERVAL '7' DAY) "+
    "GROUP BY DATE(fecha) "+
    "ORDER BY fecha DESC " , nativeQuery = true)
    List<Object[]> ventaUltimos7Dias();


}