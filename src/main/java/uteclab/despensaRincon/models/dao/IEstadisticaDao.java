package uteclab.despensaRincon.models.dao;

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

   /* @Query(" SELECT new uteclab.despensaRincon.entities.Estadistica(COALESCE(c.nombre,'a'), COALESCE(SUM (lv.cantidad),0)) " +
            " FROM Producto p " +
            " JOIN linea_venta lv ON p.id = lv.producto_id " +
            " JOIN venta_lineas_venta vlv ON lv.id = vlv.lineas_venta_id " +
            " JOIN venta v ON vlv.venta_id = v.id " +
            " JOIN Categoria c ON p.categoria_id = c.id " +
            " GROUP BY c.nombre ")
    List<Estadistica> cantVentasPorCategoria (@Param("fechaInicial") Date fechaInicial,
                                              @Param("fechaFinal") Date fechaFinal);

    */
   @Query("SELECT  new uteclab.despensaRincon.entities.Estadistica('' AS nombre, CAST (0 as Float),   " +
           "(SELECT CAST(COALESCE(SUM(v.total), 0) as Float) FROM Venta v WHERE v.fecha BETWEEN :fechaInicial AND :fechaFinal) AS cant1, " +
           "(SELECT CAST(COALESCE(SUM(c.total), 0) as Float) FROM Compra c WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal)) AS cant2 ")
   Estadistica ganancias(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
}
