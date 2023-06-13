package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.entities.Estadistica;
import uteclab.despensaRincon.models.services.EstadisticaService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @GetMapping("/3/{fechaI},{fechaF}")
    public ResponseEntity<?> ganancia (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicial = null;
        Date fechaFinal = null;
        try {
            fechaInicial = dateFormat.parse(fechaI);
            fechaFinal = dateFormat.parse(fechaF);
        } catch (ParseException e) {
            response.put("error", "Error al analizar las fechas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (fechaInicial == null) {
            error.add("No se ingreso una fecha inicial");
        }
        if (fechaFinal == null) {
            error.add("No se ingreso una fecha final");
        }
        if (fechaInicial.after(fechaFinal)) {
            error.add("La fecha inicial debe ser menor a la fecha final");
        } else {
            if (fechaInicial.equals(fechaFinal)) {
                error.add("Las fechas no pueden ser las mismas");
            }
        }
        if (error.size() > 0) {
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        Estadistica resultado = estadisticaService.ganancia(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/2/{fechaI},{fechaF}")
    public ResponseEntity<?> cantVentasPorCategoria (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicial = null;
        Date fechaFinal = null;
        try {
            fechaInicial = dateFormat.parse(fechaI);
            fechaFinal = dateFormat.parse(fechaF);
        } catch (ParseException e) {
            response.put("error", "Error al analizar las fechas");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (fechaInicial == null) {
            error.add("No se ingreso una fecha inicial");
        }
        if (fechaFinal == null) {
            error.add("No se ingreso una fecha final");
        }
        if (fechaInicial.after(fechaFinal)) {
            error.add("La fecha inicial debe ser menor a la fecha final");
        } else {
            if (fechaInicial.equals(fechaFinal)) {
                error.add("Las fechas no pueden ser las mismas");
            }
        }
        if (error.size() > 0) {
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        List<Estadistica> resultado = (List<Estadistica>) estadisticaService.cantVentasPorCategoria(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
        /*
        List<Float>  reporte = null;
        try {
            Object reporteService;
            reporte = reporteService.reporteGanancias( fechaInicial,fechaFinal);
            response.put("venta",reporte.get(0));
            response.put("compra",reporte.get(1));
            response.put("ganancia",(reporte.get(0)-reporte.get(1)));
        } catch (DataAccessException e) {
            response.put("msg", "Error al acceder a la base de datos");
            error.add(e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    */


}
