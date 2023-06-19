package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uteclab.despensaRincon.entities.Producto;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {
    public Producto findByNombre(String nombre);

    // SELECT * FROM producto WHERE LOWER(nombre) LIKE
    public List<Producto> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.stock < p.stock_minimo")
    public List<Producto> findBajoStock();

    /*
        @Query(value="SELECT p FROM Producto p "+
            "JOIN producto_proveedores pp ON p.id = pp.producto_id "+
            "WHERE pp.proveedores_id = :proveedor_id ",nativeQuery = true)
    */
    ///@Query(" SELECT p.* FROM Producto p JOIN p.proveedores pr WHERE pr.id = :proveedor_id ")
    @Query("SELECT p FROM Producto p JOIN p.proveedores pr WHERE pr.id = :prov_id")
    List<Producto> findByProveedor(@Param("prov_id") Long proveedor_id);

}

