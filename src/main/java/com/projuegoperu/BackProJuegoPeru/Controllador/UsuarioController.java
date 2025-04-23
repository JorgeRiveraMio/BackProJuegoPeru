package com.projuegoperu.BackProJuegoPeru.Controllador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuarioController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello-secured1")
    public String helloSecured1() {
        return "cliente";
    }
    @GetMapping("/hello-secured2")
    public String helloSecured2() {
        return "admin";
    }
    @GetMapping("/hello-secured3")
    public String helloSecured3() {
        return "terapeuta";
    }
}
