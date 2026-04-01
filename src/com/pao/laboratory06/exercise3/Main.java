package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 1. Creez un array de ingineri
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0711111111", 9000, 15000),
                new Inginer("Ionescu", "Vlad", "0722222222", 12000, 18000),
                new Inginer("Georgescu", "Maria", "0733333333", 8500, 12000)
        };

        // 2. Sortare naturală după nume
        System.out.println("=== Sortare naturală după nume ===");
        Arrays.sort(ingineri);
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        // 3. Sortare alternativă după salariu descrescător
        System.out.println("\n=== Sortare după salariu descrescător ===");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        // 4. Acces prin referință de tip interfață
        System.out.println("\n=== Acces prin referință PlataOnline ===");
        PlataOnline contInginer = new Inginer("Marin", "Paul", "0744444444", 10000, 20000);
        contInginer.autentificare("paul.marin", "parola123");
        System.out.println("Sold curent: " + contInginer.consultareSold());
        System.out.println("Plată 5000: " + contInginer.efectuarePlata(5000));
        System.out.println("Sold după plată: " + contInginer.consultareSold());

        // 5. Persoană juridică prin PlataOnlineSMS
        System.out.println("\n=== Persoană juridică și SMS ===");
        PlataOnlineSMS firma = new PersoanaJuridica("Tech", "SRL", "0755555555", 50000);
        firma.autentificare("tech.srl", "firma123");
        System.out.println("Sold firmă: " + firma.consultareSold());
        System.out.println("SMS trimis: " + firma.trimiteSMS("Plata a fost procesată."));
        System.out.println("SMS gol: " + firma.trimiteSMS(""));

        PersoanaJuridica firmaConcreta = (PersoanaJuridica) firma;
        System.out.println("Mesaje trimise: " + firmaConcreta.getSmsTrimise());

        // 6. Caz fără telefon
        System.out.println("\n=== Caz fără telefon ===");
        PlataOnlineSMS firmaFaraTelefon = new PersoanaJuridica("NoPhone", "SRL", "", 30000);
        System.out.println("SMS trimis fără telefon: " + firmaFaraTelefon.trimiteSMS("Mesaj test"));

        // 7. Enum cu constante financiare
        System.out.println("\n=== Constante financiare ===");
        System.out.println("TVA = " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim = " + ConstanteFinanciare.SALARIU_MINIM.getValoare());

        // 8. Tratarea erorilor
        System.out.println("\n=== Tratarea erorilor ===");
        try {
            contInginer.autentificare(null, "1234");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        try {
            contInginer.efectuarePlata(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare plată: " + e.getMessage());
        }

        try {
            PlataOnline doarCont = new Inginer("Test", "User", "0700000000", 7000, 10000);
            throw new UnsupportedOperationException("Entitatea nu are capabilitate SMS.");
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare SMS: " + e.getMessage());
        }
    }
}