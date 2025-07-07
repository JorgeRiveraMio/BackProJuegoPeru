package com.projuegoperu.BackProJuegoPeru.Controllador;


import com.projuegoperu.BackProJuegoPeru.Models.Entity.Pago;
import com.projuegoperu.BackProJuegoPeru.Services.PagoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
@CrossOrigin(origins = "*")
public class PagoController {
    @Autowired
    private PagoServices pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(pagoService.findAll());
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
