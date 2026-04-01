package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User-ul și parola nu pot fi null sau goale.");
        }
        System.out.println("Autentificare reușită pentru inginerul " + nume + " " + prenume);
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
    public int compareTo(Inginer other) {
        int cmp = this.nume.compareTo(other.nume);
        if (cmp != 0) {
            return cmp;
        }
        return this.prenume.compareTo(other.prenume);
    }

    @Override
    public String toString() {
        return "Inginer{nume='" + nume + "', prenume='" + prenume + "', salariu=" + salariu + ", sold=" + sold + "}";
    }
}