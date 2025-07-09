package com.projuegoperu.BackProJuegoPeru.Controllador;


import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.CrearPagoRequest;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Pago;
import com.projuegoperu.BackProJuegoPeru.Services.MercadoPagoService;
import com.projuegoperu.BackProJuegoPeru.Services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
@CrossOrigin(origins = "*")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @PostMapping("/preferencia")
    public ResponseEntity<String> generarPreferencia(@RequestBody CrearPagoRequest request) {
        try {
            String referenciaUnica = "pago_" + System.currentTimeMillis(); // o UUID

            // Crear el registro en la BD
            pagoService.crearPagoInicial(request, referenciaUnica);

            // Crear preferencia en Mercado Pago
            Preference preferencia = mercadoPagoService.crearPreferencia(
                    request.getDescripcion(),
                    request.getMonto(),
                    referenciaUnica // importante para el webhook
            );

            return ResponseEntity.ok(preferencia.getInitPoint());
        }catch (MPException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar la preferencia: " + e.getMessage());
        }
        catch (Exception  e) {

            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al crear preferencia");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getById(@PathVariable Integer id) {
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.save(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> update(@PathVariable Integer id, @RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.update(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pagoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
