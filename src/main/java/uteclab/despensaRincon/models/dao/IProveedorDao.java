package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uteclab.despensaRincon.entities.Compra;
import uteclab.despensaRincon.entities.Proveedor;

import java.util.Date;
import java.util.List;

public interface IProveedorDao extends CrudRepository<Proveedor,Long> {
    public List<Proveedor> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);
    /*
    @Modifying
    @Query(value = "UPDATE compra SET proveedor_id = NULL WHERE proveedor_id = :id ; " +
            "DELETE FROM producto_proveedores WHERE proveedores_id = :id ; " +
            "DELETE FROM proveedor_vendedores WHERE proveedor_id = :id ;", nativeQuery = true)
    int quitarRelacionesProveedor(@Param("id") Long id);
     */
}
