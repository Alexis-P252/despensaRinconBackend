package uteclab.despensaRincon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.annotations.VerificarToken;
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
///1
    @VerificarToken
    @GetMapping("/1")
    public ResponseEntity<?> cantProductosPorCategoria(){
        Map<String, Object> response = new HashMap<>();
        List<Estadistica> resultado = estadisticaService.cantProductosPorCategoria();
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///2
    @VerificarToken
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
        List<Estadistica> resultado =  estadisticaService.cantVentasPorCategoria(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///3
    @VerificarToken
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
///4
    @VerificarToken
    @GetMapping("/4/{fechaI},{fechaF}")
    public ResponseEntity<?> productosMasVendidos (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
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
        List <Estadistica> resultado = estadisticaService.productosMasVendidos(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///5
    @VerificarToken
    @GetMapping("/5/{fechaI},{fechaF}")
    public ResponseEntity<?> productosMasComprados (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
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
        List <Estadistica> resultado = estadisticaService.productosMasComprados(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///6
    @VerificarToken
    @GetMapping("/6/{fechaI},{fechaF}")
    public ResponseEntity<?> cantVentaClientesRegulares (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
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
        Estadistica resultado = estadisticaService.cantVentaClientesRegulares(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///7
    @VerificarToken
    @GetMapping("/7/{fechaI},{fechaF}")
    public ResponseEntity<?> comprasProveedores (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
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
        List<Estadistica> resultado = estadisticaService.comprasProveedores(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///8
    @VerificarToken
    @GetMapping("/8/{fechaI},{fechaF}")
    public ResponseEntity<?> mejoresClientes (@PathVariable(value = "fechaI") String fechaI, @PathVariable(value = "fechaF") String fechaF) {
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
        List<Estadistica> resultado = estadisticaService.mejoresClientes(fechaInicial, fechaFinal);
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
///9 ventaUltimos7Dias
    @VerificarToken
    @GetMapping("/9")
    public ResponseEntity<?> ventaUltimos7Dias(){
        Map<String, Object> response = new HashMap<>();
        List<Estadistica> resultado = estadisticaService.ventaUltimos7Dias();
        response.put("resultado", resultado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
