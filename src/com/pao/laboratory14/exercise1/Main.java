package com.pao.laboratory14.exercise1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Bilet> bilete = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");

            int id = Integer.parseInt(parts[0]);
            String eveniment = parts[1];
            TipBilet tip = TipBilet.valueOf(parts[2]);
            double pret = Double.parseDouble(parts[3]);

            bilete.add(new Bilet(id, eveniment, tip, pret));
        }

        String comanda = scanner.nextLine().trim();

        RaportVanzari raport = bilete.stream()
                .collect(RaportVanzariCollector.toRaportVanzari());

        if ("RAPORT_SIMPLU".equals(comanda)) {
            afiseazaRaportSimplu(raport);
        } else if ("RAPORT_COMPLET".equals(comanda)) {
            afiseazaRaportSimplu(raport);
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON%n", raport.getTotalGlobal());
            System.out.printf(Locale.US, "Medie: %.2f RON%n", raport.getMedieGlobala());
            System.out.println("Cel mai popular: " + raport.getTipCelMaiPopular());
        }
    }

    private static void afiseazaRaportSimplu(RaportVanzari raport) {
        for (TipBilet tip : TipBilet.values()) {
            if (raport.getNumarPerTip().containsKey(tip)) {
                long count = raport.getNumarPerTip().get(tip);
                double incasari = raport.getIncasariPerTip().get(tip);

                System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON%n", tip, count, incasari);
            }
        }
    }
}