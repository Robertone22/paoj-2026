package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (sc.hasNextLine()) {
            String linie = sc.nextLine().trim();

            if (linie.isEmpty()) {
                continue;
            }

            String[] parts = linie.split("\\s+");
            String comanda = parts[0];

            switch (comanda) {
                case "ENQUEUE": {
                    int id = Integer.parseInt(parts[1]);
                    double suma = Double.parseDouble(parts[2]);
                    String data = parts[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parts[4]);

                    coada.addLast(new Tranzactie(id, suma, data, tip));
                    break;
                }

                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Procesat: " + t);
                    }
                    break;
                }

                case "PUSH": {
                    int id = Integer.parseInt(parts[1]);
                    double suma = Double.parseDouble(parts[2]);
                    String data = parts[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parts[4]);

                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                    break;
                }

                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Extras: " + t);
                    }
                    break;
                }

                case "REMOVE_DEBIT": {
                    int count = 0;
                    Iterator<Tranzactie> itr = coada.iterator();

                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            count++;
                        }
                    }

                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                    break;
                }

                case "REMOVE_BELOW": {
                    double prag = Double.parseDouble(parts[1]);
                    int count = 0;
                    Iterator<Tranzactie> itr = coada.iterator();

                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < prag) {
                            itr.remove();
                            count++;
                        }
                    }

                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", count, prag);
                    break;
                }

                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }

                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }

                default:
                    break;
            }
        }
    }
}