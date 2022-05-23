package com.project.request_credit.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreditModel {
    private double capital;
    private int duree;
    private double taux;
    private double mensualite;
}
