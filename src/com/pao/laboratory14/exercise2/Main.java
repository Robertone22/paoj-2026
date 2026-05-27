package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new File("output").mkdirs();

        EvenimentRepository repository = new EvenimentRepository();
        repository.initSchema();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            handleCommand(line, repository);
        }
    }

    private static void handleCommand(String line, EvenimentRepository repository) {
        String[] parts = line.split("\\s+");
        String command = parts[0];

        switch (command) {
            case "ADD" -> handleAdd(parts, repository);
            case "LIST" -> handleList(repository);
            case "DELETE" -> handleDelete(parts, repository);
            case "COUNT" -> handleCount(repository);
            default -> {
                // Unknown commands are ignored.
            }
        }
    }

    private static void handleAdd(String[] parts, EvenimentRepository repository) {
        String nume = parts[1];
        String data = parts[2];
        int capacitate = Integer.parseInt(parts[3]);
        TipBilet tip = TipBilet.valueOf(parts[4]);

        Eveniment eveniment = new Eveniment(nume, data, capacitate, tip);
        repository.save(eveniment);

        System.out.println("Adaugat: [" + eveniment.getId() + "] " + eveniment.getNume());
    }

    private static void handleList(EvenimentRepository repository) {
        List<Eveniment> evenimente = repository.findAll();

        for (Eveniment eveniment : evenimente) {
            System.out.println(eveniment);
        }
    }

    private static void handleDelete(String[] parts, EvenimentRepository repository) {
        int id = Integer.parseInt(parts[1]);
        int deletedRows = repository.deleteImpl(id);

        if (deletedRows > 0) {
            System.out.println("Sters: " + id);
        } else {
            System.out.println("Nu exista: " + id);
        }
    }

    private static void handleCount(EvenimentRepository repository) {
        System.out.println("Total: " + repository.count());
    }
}