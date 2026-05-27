package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProtocolEngine engine = new ProtocolEngine();

        String firstLine = readNonEmptyLine(scanner);
        if (firstLine == null) {
            return;
        }

        int q = Integer.parseInt(firstLine);
        int processedCommands = 0;

        while (processedCommands < q && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String result = engine.process(line);

            if (result != null) {
                System.out.println(result);
            }

            processedCommands++;
        }
    }

    private static String readNonEmptyLine(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (!line.isEmpty()) {
                return line;
            }
        }

        return null;
    }
}