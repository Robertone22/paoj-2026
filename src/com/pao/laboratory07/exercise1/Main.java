package com.pao.laboratory07.exercise1;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        StareComanda stareCurenta = StareComanda.valueOf(in.nextLine().trim());
        Deque<StareComanda> istoric = new ArrayDeque<>();

        System.out.println("Initial order state: " + stareCurenta);

        while (in.hasNextLine()) {
            String comanda = in.nextLine().trim();

            if (comanda.equals("QUIT")) {
                System.out.println("User quit the program.");
                break;
            }

            if (comanda.equals("undo")) {
                if (istoric.isEmpty()) {
                    System.out.println("Nu există stare anterioară pentru undo.");
                } else {
                    stareCurenta = istoric.pop();
                    System.out.println("Order state reverted to: " + stareCurenta);
                }
                continue;
            }

            if (comanda.equals("next")) {
                if (stareCurenta.esteFinala()) {
                    System.out.println("Order is already in a final state.");
                } else {
                    istoric.push(stareCurenta);
                    stareCurenta = stareCurenta.next();
                    System.out.println("Order state updated to: " + stareCurenta);
                }
                continue;
            }

            if (comanda.equals("cancel")) {
                if (stareCurenta.esteFinala()) {
                    System.out.println("Cannot cancel a final state order.");
                } else {
                    istoric.push(stareCurenta);
                    stareCurenta = StareComanda.CANCELED;
                    System.out.println("Order has been canceled.");
                }
            }
        }
    }
}