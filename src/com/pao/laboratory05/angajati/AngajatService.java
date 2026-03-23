package com.pao.laboratory05.angajati;

import java.util.Arrays;


public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[angajati.length] = a;
        angajati = newAngajati;

        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu există angajați.");
            return;
        }

        for (int i = 0; i < angajati.length; i++) {
            System.out.println((i + 1) + ". " + angajati[i]);
        }
    }

    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu există angajați.");
            return;
        }

        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        System.out.println("--- Angajați după salariu (descrescător) ---");
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean found = false;

        System.out.println("--- Angajați din " + numeDept + " ---");
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(angajat);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}