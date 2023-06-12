package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.services.EstadisticaService;

import java.util.List;

@RestController
@RequestMapping("/dr/estadistica")
@CrossOrigin(origins = "*")
public class EstadisticaController {

    @Autowired
    private EstadisticaService estadisticaService;
    @GetMapping("/1")
    public List<Estadistica> cantProductosPorCategoria(){
        return estadisticaService.cantProductosPorCategoria();
    }

}
