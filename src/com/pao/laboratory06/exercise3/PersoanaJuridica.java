package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
        this.smsTrimise = new ArrayList<>();
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User-ul și parola nu pot fi null sau goale.");
        }
        System.out.println("Autentificare reușită pentru persoana juridică " + nume + " " + prenume);
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă.");
        }
        if (suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isBlank()) {
            return false;
        }
        if (telefon == null || telefon.isBlank()) {
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }

    @Override
    public String toString() {
        return "PersoanaJuridica{nume='" + nume + "', prenume='" + prenume + "', telefon='" + telefon + "', sold=" + sold + "}";
    }
}