package com.pao.laboratory05.angajati;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String optiune = scanner.nextLine().trim();

            switch (optiune) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine().trim();

                    System.out.print("Departament (nume): ");
                    String numeDepartament = scanner.nextLine().trim();

                    System.out.print("Departament (locatie): ");
                    String locatieDepartament = scanner.nextLine().trim();

                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine().trim());

                    Departament departament = new Departament(numeDepartament, locatieDepartament);
                    Angajat angajat = new Angajat(nume, departament, salariu);
                    service.addAngajat(angajat);
                    break;

                case "2":
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Departament: ");
                    String dept = scanner.nextLine().trim();
                    service.findByDepartament(dept);
                    break;

                case "0":
                    System.out.println("La revedere!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }
}