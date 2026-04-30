package com.pao.laboratory09.exercise1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_PATH = "output/lab09_ex1.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            int n = Integer.parseInt(sc.nextLine().trim());
            List<Tranzactie> tranzactii = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                String[] parts = sc.nextLine().trim().split("\\s+");

                int id = Integer.parseInt(parts[0]);
                double suma = Double.parseDouble(parts[1]);
                String data = parts[2];
                String contSursa = parts[3];
                String contDestinatie = parts[4];
                TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

                Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
                t.setNote("procesat");
                tranzactii.add(t);
            }

            serializeaza(tranzactii);
            List<Tranzactie> restaurate = deserializeaza();

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] cmd = line.split("\\s+");

                switch (cmd[0]) {
                    case "LIST":
                        for (Tranzactie t : restaurate) {
                            System.out.println(t);
                        }
                        break;

                    case "FILTER":
                        String prefix = cmd[1];
                        boolean gasit = false;

                        for (Tranzactie t : restaurate) {
                            if (t.getData().startsWith(prefix)) {
                                System.out.println(t);
                                gasit = true;
                            }
                        }

                        if (!gasit) {
                            System.out.println("Niciun rezultat.");
                        }
                        break;

                    case "NOTE":
                        int idCautat = Integer.parseInt(cmd[1]);
                        Tranzactie tranzactieGasita = null;

                        for (Tranzactie t : restaurate) {
                            if (t.getId() == idCautat) {
                                tranzactieGasita = t;
                                break;
                            }
                        }

                        if (tranzactieGasita == null) {
                            System.out.println("NOTE[" + idCautat + "]: not found");
                        } else {
                            System.out.println("NOTE[" + idCautat + "]: " + tranzactieGasita.getNote());
                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void serializeaza(List<Tranzactie> tranzactii) throws Exception {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tranzactii);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Tranzactie> deserializeaza() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Tranzactie>) in.readObject();
        }
    }
}