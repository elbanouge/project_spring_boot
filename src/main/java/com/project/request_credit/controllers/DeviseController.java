package com.project.request_credit.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/convertToDevise")
    public ConversionResponse convertToDevise(@RequestParam(name = "from", defaultValue = "MAD") String fromCurrency,
            @RequestParam("to") String toCurrency,
            @RequestParam("amount") Double amount) {

        if (!fromCurrency.equalsIgnoreCase("MAD")) {
            throw new UnknownCurrencyException("Unknown 'from' currency: " + fromCurrency + ", only MAD supported.");
        }

        Double factor = conversionFactorFor(toCurrency);
        Double converted = amount / factor;
        return new ConversionResponse(toCurrency, converted);
    }

    @GetMapping("/convertToMAD")
    public ConversionResponse convertToMad(@RequestParam(name = "to", defaultValue = "MAD") String toCurrency,
            @RequestParam("from") String fromCurrency,
            @RequestParam("amount") Double amount) {

        if (!toCurrency.equalsIgnoreCase("MAD")) {
            throw new UnknownCurrencyException("Unknown 'from' currency: " + fromCurrency + ", only MAD supported.");
        }

        Double factor = conversionFactorFor(fromCurrency);
        Double converted = amount * factor;
        return new ConversionResponse(fromCurrency, converted);
    }

    private Double conversionFactorFor(String toCurrency) {
        Double factor;
        Float tauxChange = 0.0f;

        if (toCurrency.equalsIgnoreCase("USD")) {
            tauxChange = getTauxChange("USD");
            factor = Double.valueOf(tauxChange);// 1 USD = 9.88 MAD
        } else if (toCurrency.equalsIgnoreCase("EUR")) {
            tauxChange = getTauxChange("EUR");
            factor = Double.valueOf(tauxChange); // 1 EUR = 10.59 MAD
        } else if (toCurrency.equalsIgnoreCase("JPY")) {
            tauxChange = getTauxChange("JPY");
            factor = Double.valueOf(tauxChange); // 1 JPY = 0.075 MAD
        } else if (toCurrency.equalsIgnoreCase("GBP")) {
            tauxChange = getTauxChange("GBP");
            factor = Double.valueOf(tauxChange); // 1 GBP = 12.34 MAD
        } else if (toCurrency.equalsIgnoreCase("CHF")) {
            tauxChange = getTauxChange("CHF");
            factor = Double.valueOf(tauxChange); // 1 CHF = 10.27 MAD
        } else {
            throw new UnknownCurrencyException(
                    "Unknown 'to' currency: " + toCurrency + ". Must be one of USD, EUR, JPY, GBP, or CHF.");
        }
        return factor;
    }

    @SuppressWarnings("unchecked")
    public Float getTauxChange(String currency) {
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", "71fe3a8e1c2a4ed88a26a9788813129d");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<?> response = restTemplate.exchange(URL_BASE + "?libDevise=" + currency,
                HttpMethod.GET,
                entity,
                List.class);

        List<Map<String, Object>> result = (List<Map<String, Object>>) response.getBody();

        System.out.println("result: " + result);

        if (result.isEmpty()) {
            throw new UnknownCurrencyException(
                    "Unknown 'to' currency: " + currency + ". Must be one of USD, EUR, JPY, GBP, or CHF.");
        } else {
            System.out.println("****** 1 " + currency + " = " + result.get(0).get("venteClientele") + " MAD");
            return Float.parseFloat(result.get(0).get("venteClientele").toString());
        }
    }

}
