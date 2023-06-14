package uteclab.despensaRincon.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uteclab.despensaRincon.entities.Proveedor;
import uteclab.despensaRincon.entities.Vendedor;

import java.util.List;

public interface IVendedorDao extends CrudRepository<Vendedor,Long> {
    public List<Vendedor> findByNombreContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombre, String correo);

    @Query("SELECT p FROM Proveedor p JOIN p.vendedores v WHERE v.id = :vendedorId")
    List<Proveedor> findAllByVendedorId(Long vendedorId);

}
