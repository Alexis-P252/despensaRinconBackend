package uteclab.despensaRincon.models.services;

import uteclab.despensaRincon.entities.ClienteRegular;

import java.util.List;

public interface IClienteRegularService {
    public List<ClienteRegular> findAll();
    public ClienteRegular findById(Long id);

    public ClienteRegular findByCedula(String cedula);
    public ClienteRegular save(ClienteRegular cr);
    public void deleteById(Long id);
    public List<String> findAllCorreos();
    public List<ClienteRegular> buscarClientesRegulares (String cedula, String nombre, String correo);

}
