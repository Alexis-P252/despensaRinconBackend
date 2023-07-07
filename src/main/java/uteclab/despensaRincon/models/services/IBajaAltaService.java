package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.Aviso;
import uteclab.despensaRincon.entities.BajaAlta;

import java.util.List;

public interface IBajaAltaService {
    public List<BajaAlta> findAll();

    public BajaAlta findById(Long id);
    public BajaAlta save (BajaAlta bajaAlta);
    public void deleteByObject (BajaAlta bajaAlta);
}
