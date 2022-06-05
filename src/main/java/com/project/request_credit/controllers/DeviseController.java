package com.project.request_credit.controllers;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.project.request_credit.exceptions.UnknownCurrencyException;
import com.project.request_credit.models.ConversionResponse;

@RequestMapping("/api/devise")
@RestController
public class DeviseController {

    static final String URL_BASE = "https://api.centralbankofmorocco.ma/cours/Version1/api/CoursBBE";

    @GetMapping("/convert")
    public ConversionResponse convert(@RequestParam(name = "from", defaultValue = "MAD") String fromCurrency,
            @RequestParam("to") String toCurrency,
            @RequestParam("amount") BigDecimal amount) {

        if (!fromCurrency.equalsIgnoreCase("MAD")) {
            throw new UnknownCurrencyException("Unknown 'from' currency: " + fromCurrency + ", only MAD supported.");
        }
        BigDecimal factor = conversionFactorFor(toCurrency);
        return new ConversionResponse(toCurrency, factor.multiply(amount));
    }

    private BigDecimal conversionFactorFor(String toCurrency) {
        BigDecimal factor;
        Float tauxChange = 0.0f;

        if (toCurrency.equalsIgnoreCase("BTC")) {
            tauxChange = getTauxChange("BTC");
            factor = BigDecimal.valueOf(tauxChange); // 1 MAD = 294811.87 BTC
        } else if (toCurrency.equalsIgnoreCase("USD")) {
            tauxChange = getTauxChange("USD");
            factor = BigDecimal.valueOf(tauxChange);// 1 MAD = 9.88 USD
        } else if (toCurrency.equalsIgnoreCase("EUR")) {
            tauxChange = getTauxChange("EUR");
            factor = BigDecimal.valueOf(tauxChange); // 1 MAD = 10.59 EUR
        } else if (toCurrency.equalsIgnoreCase("JPY")) {
            tauxChange = getTauxChange("JPY");
            factor = BigDecimal.valueOf(tauxChange); // 1 MAD = 0.075 JPY
        } else if (toCurrency.equalsIgnoreCase("GBP")) {
            tauxChange = getTauxChange("GBP");
            factor = BigDecimal.valueOf(tauxChange); // 1 MAD = 12.34 GBP
        } else if (toCurrency.equalsIgnoreCase("CHF")) {
            tauxChange = getTauxChange("CHF");
            factor = BigDecimal.valueOf(tauxChange); // 1 MAD = 10.27 CHF
        } else {
            throw new UnknownCurrencyException(
                    "Unknown 'to' currency: " + toCurrency + ". Must be one of BTC, USD, EUR, JPY, GBP, or CHF.");
        }
        return factor;
    }

    public Float getTauxChange(String currency) {
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", "71fe3a8e1c2a4ed88a26a9788813129d");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(URL_BASE + "?libDevise=" + currency, HttpMethod.GET,
                entity,
                String.class);

        String result = response.getBody();
        System.out.println("result: " + result);
        result = "12.34";
        return Float.parseFloat(result);
    }

}
