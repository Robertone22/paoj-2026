package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected double pret;
    protected OrderState stare;

    public Comanda(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
        this.stare = OrderState.PLACED;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    public OrderState getStare() {
        return stare;
    }

    public abstract double pretFinal();

    public abstract String descriere();
}