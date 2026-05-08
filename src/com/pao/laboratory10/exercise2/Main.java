package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");

            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            TipTranzactie tip = TipTranzactie.valueOf(parts[3]);

            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] cmd = line.split("\\s+");
            String comanda = cmd[0];

            switch (comanda) {
                case "UNIQUE_IDS":
                    rezolvaUniqueIds(tranzactii);
                    break;

                case "MONTHLY_REPORT":
                    rezolvaMonthlyReport(tranzactii);
                    break;

                case "TOP":
                    int topN = Integer.parseInt(cmd[1]);
                    rezolvaTop(tranzactii, topN);
                    break;

                case "SORT_ASC":
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    afiseazaLista(tranzactii);
                    break;

                case "SORT_DESC":
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    afiseazaLista(tranzactii);
                    break;

                case "REVERSE":
                    Collections.reverse(tranzactii);
                    afiseazaLista(tranzactii);
                    break;

                case "MIN_MAX":
                    rezolvaMinMax(tranzactii);
                    break;

                case "CME_DEMO":
                    rezolvaCmeDemo(tranzactii);
                    break;

                default:
                    break;
            }
        }
    }

    private static void rezolvaUniqueIds(List<Tranzactie> tranzactii) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();

        for (Tranzactie t : tranzactii) {
            ids.add(t.getId());
        }

        System.out.println("IDs unice (" + ids.size() + "): " + ids);
    }

    private static void rezolvaMonthlyReport(List<Tranzactie> tranzactii) {
        TreeMap<String, double[]> raport = new TreeMap<>();

        for (Tranzactie t : tranzactii) {
            String luna = t.getData().substring(0, 7);
            raport.putIfAbsent(luna, new double[]{0.0, 0.0});

            if (t.getTip() == TipTranzactie.CREDIT) {
                raport.get(luna)[0] += t.getSuma();
            } else {
                raport.get(luna)[1] += t.getSuma();
            }
        }

        for (String luna : raport.keySet()) {
            double sumaCredit = raport.get(luna)[0];
            double sumaDebit = raport.get(luna)[1];

            System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n", luna, sumaCredit, sumaDebit);
        }
    }

    private static void rezolvaTop(List<Tranzactie> tranzactii, int n) {
        List<Tranzactie> copie = new ArrayList<>(tranzactii);
        Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());

        int limita = Math.min(n, copie.size());

        System.out.println("Top " + n + ":");
        for (int i = 0; i < limita; i++) {
            System.out.println(copie.get(i));
        }
    }

    private static void rezolvaMinMax(List<Tranzactie> tranzactii) {
        if (tranzactii.isEmpty()) {
            return;
        }

        Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
        Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));

        System.out.println("MIN: " + min);
        System.out.println("MAX: " + max);
    }

    private static void rezolvaCmeDemo(List<Tranzactie> tranzactii) {
        List<Tranzactie> copie = new ArrayList<>(tranzactii);

        try {
            for (Tranzactie t : copie) {
                copie.remove(t);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
        }
    }

    private static void afiseazaLista(List<Tranzactie> tranzactii) {
        for (Tranzactie t : tranzactii) {
            System.out.println(t);
        }
    }
}