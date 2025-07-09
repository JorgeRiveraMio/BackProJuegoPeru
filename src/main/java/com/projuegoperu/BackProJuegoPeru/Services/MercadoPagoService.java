package com.projuegoperu.BackProJuegoPeru.Services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public Preference crearPreferencia(String titulo, BigDecimal monto, String referencia) throws MPException, MPApiException {
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(titulo)
                .quantity(1)
                .currencyId("PEN")
                .unitPrice(monto)
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://projuegoperu.vercel.app//pago-exitoso")
                .failure("https://projuegoperu.vercel.app//failure")
                .pending("https://projuegoperu.vercel.app//pending")
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .autoReturn("approved")
                .externalReference(referencia)
                .build();

        return new com.mercadopago.client.preference.PreferenceClient().create(preferenceRequest);
    }

}