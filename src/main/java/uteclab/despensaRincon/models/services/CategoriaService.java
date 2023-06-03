package uteclab.despensaRincon.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteclab.despensaRincon.entities.Categoria;
import uteclab.despensaRincon.entities.ClienteRegular;
import uteclab.despensaRincon.models.dao.ICategoriaDao;

import java.util.List;
@Service
public class CategoriaService implements ICategoriaService {
    @Autowired
    private ICategoriaDao categoriaDao;

    @Override
    public List<Categoria> findAll() {
        return (List<Categoria>) categoriaDao.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaDao.findById(id).orElse(null);
    }

    @Override
    public Categoria save(Categoria ca) {
        return categoriaDao.save(ca);
    }

    @Override
    public void deleteById(Long id) {
        categoriaDao.deleteById(id);
    }
    @Override
    public Categoria findByNombre (String nombre){ return categoriaDao.findByNombre(nombre);}
    @Override
    public List<Categoria> buscarCategorias(String query) {
        return categoriaDao.findByNombreContainingIgnoreCase(query);
    }

}
