package com.projuegoperu.BackProJuegoPeru.Services;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.CrearPagoRequest;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.*;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoPago;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.MetodoPago;
import com.projuegoperu.BackProJuegoPeru.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private SesionRepository sesionRepository;

    public Pago crearPagoInicial(CrearPagoRequest request, String referencia) {
        Tutor tutor = tutorRepository.findById(request.getTutorId().intValue())
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));

        Sesion sesion = sesionRepository.findById(request.getSesionId().intValue())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Pago pago = new Pago();
        pago.setMonto(request.getMonto());
        pago.setMetodoPago(MetodoPago.MERCADO_PAGO); 
        pago.setEstado(EstadoPago.PENDIENTE); 
        pago.setFechaPago(LocalDateTime.now());
        pago.setSesion(sesion);
        pago.setTutor(tutor);
        pago.setReferenciaPago(referencia);

        return pagoRepository.save(pago);
    }

    public void procesarPago(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        Payment pagoMP = client.get(paymentId);

        // Buscar por referencia de pago
        String referencia = pagoMP.getExternalReference(); // Lo seteamos cuando creamos la preferencia
        Optional<Pago> pagoLocalOpt = pagoRepository.findByReferenciaPago(referencia);

        if (pagoLocalOpt.isPresent()) {
            Pago pago = pagoLocalOpt.get();

            // Actualizar estado según pago en MP
            switch (pagoMP.getStatus()) {
                case "approved":
                    pago.setEstado(EstadoPago.CONFIRMADO);
                    break;
                case "pending":
                    pago.setEstado(EstadoPago.PENDIENTE);
                    break;
                case "rejected":
                    pago.setEstado(EstadoPago.RECHAZADO);
                    break;
                default:
                    pago.setEstado(EstadoPago.PENDIENTE);
            }

            pagoRepository.save(pago);
        } else {
            System.out.println("Pago no encontrado con referencia: " + referencia);
        }
    }

    public Optional<Pago> findById(Integer id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago update(Integer id, Pago pago) {
        pago.setId(id);
        return pagoRepository.save(pago);
    }

    public void deleteById(Integer id) {
        pagoRepository.deleteById(id);
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }
}
