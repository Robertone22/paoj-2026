package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    static class Tranzactie {
        private int id;
        private double suma;
        private String data;
        private String contSursa;
        private TipTranzactie tip;

        public Tranzactie(int id, double suma, String data, String contSursa, TipTranzactie tip) {
            this.id = id;
            this.suma = suma;
            this.data = data;
            this.contSursa = contSursa;
            this.tip = tip;
        }

        public int getId() {
            return id;
        }

        public double getSuma() {
            return suma;
        }

        public String getData() {
            return data;
        }

        public String getContSursa() {
            return contSursa;
        }

        public TipTranzactie getTip() {
            return tip;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s %s: %.2f RON | %s",
                    id, data, tip, suma, contSursa);
        }
    }

    public static void main(String[] args) {
        List<Tranzactie> tranzactii = new ArrayList<>();

        tranzactii.add(new Tranzactie(1, 500.00, "2024-01-10", "CONT_A", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(2, 300.00, "2024-01-15", "CONT_B", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(3, 1200.00, "2024-01-28", "CONT_A", TipTranzactie.CREDIT));

        tranzactii.add(new Tranzactie(4, 700.00, "2024-02-05", "CONT_C", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(5, 150.00, "2024-02-11", "CONT_B", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(6, 950.00, "2024-02-19", "CONT_D", TipTranzactie.DEBIT));

        tranzactii.add(new Tranzactie(7, 400.00, "2024-03-03", "CONT_A", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(8, 2500.00, "2024-03-12", "CONT_E", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(9, 80.00, "2024-03-20", "CONT_C", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(10, 620.00, "2024-03-29", "CONT_B", TipTranzactie.DEBIT));

        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== 2. Total procesat ===");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", total);

        System.out.println();
        System.out.println("=== 3. Total per luna ===");
        Map<String, Double> totalPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));

        totalPerLuna.forEach((luna, suma) ->
                System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println();
        System.out.println("=== 4. Top 3 tranzactii ===");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println();
        System.out.println("=== 6. Suma medie ===");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", medie);

        System.out.println();
        System.out.println("=== 7. Extrase de cont lunare ===");
        Map<String, List<Tranzactie>> extrase = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));

        extrase.forEach((luna, lista) -> {
            double sumaLunara = lista.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), sumaLunara);
        });
    }
}