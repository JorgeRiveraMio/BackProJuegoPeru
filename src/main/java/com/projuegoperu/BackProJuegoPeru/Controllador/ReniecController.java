package com.projuegoperu.BackProJuegoPeru.Controllador;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projuegoperu.BackProJuegoPeru.Services.ReniecService;


@RestController
@CrossOrigin("*")
public class ReniecController {

    private final ReniecService reniecService;

    public ReniecController(ReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/consultar-dni")
    public String consultarDNI(@RequestParam String dni) {
        return reniecService.consultarDNI(dni);
    }
}
