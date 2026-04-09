package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split("\\s+");

            if (tokens.length == 0) {
                throw new InvalidInputException("Linie goală în input.");
            }

            switch (tokens[0]) {
                case "STANDARD" -> {
                    if (tokens.length != 4) {
                        throw new InvalidInputException("Input invalid pentru STANDARD.");
                    }
                    String nume = tokens[1];
                    double pret = Double.parseDouble(tokens[2]);
                    String client = tokens[3];
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }
                case "DISCOUNTED" -> {
                    if (tokens.length != 5) {
                        throw new InvalidInputException("Input invalid pentru DISCOUNTED.");
                    }
                    String nume = tokens[1];
                    double pret = Double.parseDouble(tokens[2]);
                    int discount = Integer.parseInt(tokens[3]);
                    String client = tokens[4];
                    comenzi.add(new ComandaRedusa(nume, pret, discount, client));
                }
                case "GIFT" -> {
                    if (tokens.length != 3) {
                        throw new InvalidInputException("Input invalid pentru GIFT.");
                    }
                    String nume = tokens[1];
                    String client = tokens[2];
                    comenzi.add(new ComandaGratuita(nume, client));
                }
                default -> throw new InvalidInputException("Tip necunoscut de comandă: " + tokens[0]);
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.equals("QUIT")) {
                break;
            }

            if (line.equals("STATS")) {
                System.out.println();
                System.out.println("--- STATS ---");

                Map<String, Double> medii = comenzi.stream()
                        .collect(Collectors.groupingBy(
                                c -> {
                                    if (c instanceof ComandaStandard) return "STANDARD";
                                    if (c instanceof ComandaRedusa) return "DISCOUNTED";
                                    return "GIFT";
                                },
                                Collectors.averagingDouble(Comanda::pretFinal)
                        ));

                if (medii.containsKey("STANDARD")) {
                    System.out.printf("STANDARD: medie = %.2f lei%n", medii.get("STANDARD"));
                }
                if (medii.containsKey("DISCOUNTED")) {
                    System.out.printf("DISCOUNTED: medie = %.2f lei%n", medii.get("DISCOUNTED"));
                }
                if (medii.containsKey("GIFT")) {
                    System.out.printf("GIFT: medie = %.2f lei%n", medii.get("GIFT"));
                }
            } else if (line.startsWith("FILTER")) {
                String[] tokens = line.split("\\s+");
                if (tokens.length != 2) {
                    throw new InvalidInputException("Comanda FILTER invalidă.");
                }

                double threshold = Double.parseDouble(tokens[1]);

                System.out.println();
                System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);

                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.println(c.descriereFaraStare()));
            } else if (line.equals("SORT")) {
                System.out.println();
                System.out.println("--- SORT (by client, then by pret) ---");

                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient)
                                .thenComparing(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriereFaraStare()));
            } else if (line.equals("SPECIAL")) {
                System.out.println();
                System.out.println("--- SPECIAL (discount > 15%) ---");

                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa)
                        .map(c -> (ComandaRedusa) c)
                        .filter(c -> c.getDiscountProcent() > 15)
                        .forEach(c -> System.out.println(c.descriereFaraStare()));
            } else {
                throw new InvalidInputException("Comandă necunoscută: " + line);
            }
        }
    }
}