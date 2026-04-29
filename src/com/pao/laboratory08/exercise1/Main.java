package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        try {
            List<Student> studenti = citesteStudentiDinFisier();

            Scanner sc = new Scanner(System.in);
            String linieComanda = sc.nextLine().trim();
            String[] tokens = linieComanda.split(" ", 2);
            String comanda = tokens[0];

            switch (comanda) {
                case "PRINT":
                    for (Student student : studenti) {
                        System.out.println(student);
                    }
                    break;

                case "SHALLOW":
                    if (tokens.length < 2) {
                        return;
                    }
                    executaShallow(studenti, tokens[1].trim());
                    break;

                case "DEEP":
                    if (tokens.length < 2) {
                        return;
                    }
                    executaDeep(studenti, tokens[1].trim());
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Student> citesteStudentiDinFisier() throws IOException {
        List<Student> studenti = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String linie;

        while ((linie = br.readLine()) != null) {
            linie = linie.trim();

            if (linie.isEmpty()) {
                continue;
            }

            String[] parts = linie.split(",");

            if (parts.length < 4) {
                continue;
            }

            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student student = new Student(nume, varsta, adresa);
            studenti.add(student);
        }

        br.close();
        return studenti;
    }

    private static Student gasesteStudent(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }
        return null;
    }

    private static void executaShallow(List<Student> studenti, String nume) throws CloneNotSupportedException {
        Student original = gasesteStudent(studenti, nume);
        if (original == null) {
            return;
        }

        Student clona = original.shallowClone();
        clona.getAdresa().setOras("MODIFICAT");

        System.out.println("Original: " + original);
        System.out.println("Clona: " + clona);
    }

    private static void executaDeep(List<Student> studenti, String nume) throws CloneNotSupportedException {
        Student original = gasesteStudent(studenti, nume);
        if (original == null) {
            return;
        }

        Student clona = original.deepClone();
        clona.getAdresa().setOras("MODIFICAT");

        System.out.println("Original: " + original);
        System.out.println("Clona: " + clona);
    }
}