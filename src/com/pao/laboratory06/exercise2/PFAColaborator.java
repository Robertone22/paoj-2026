package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private double cheltuieliLunare;

    private static final double SALARIU_MINIM_ANUAL = 4050 * 12.0;

    public PFAColaborator() {
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei%n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetBrutAnual = (venitBrutLunar - cheltuieliLunare) * 12;

        double impozit = 0.10 * venitNetBrutAnual;

        double cass;
        if (venitNetBrutAnual < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNetBrutAnual <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * venitNetBrutAnual;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_ANUAL);
        }

        double cas;
        if (venitNetBrutAnual < 12 * SALARIU_MINIM_ANUAL) {
            cas = 0;
        } else if (venitNetBrutAnual <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }

        return venitNetBrutAnual - impozit - cass - cas;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}