package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static final String FILE_PATH = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            int n = Integer.parseInt(sc.nextLine().trim());

            File file = new File(FILE_PATH);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                for (int i = 0; i < n; i++) {
                    String[] parts = sc.nextLine().trim().split("\\s+");

                    int id = Integer.parseInt(parts[0]);
                    double suma = Double.parseDouble(parts[1]);
                    String data = parts[2];
                    TipTranzactie tip = TipTranzactie.valueOf(parts[3]);

                    writeRecord(out, id, suma, data, tip, StatusTranzactie.PENDING);
                }
            }

            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] cmd = line.split("\\s+");

                    switch (cmd[0]) {
                        case "READ" -> {
                            int idx = Integer.parseInt(cmd[1]);
                            System.out.println(readRecord(raf, idx));
                        }
                        case "UPDATE" -> {
                            int idx = Integer.parseInt(cmd[1]);
                            StatusTranzactie status = StatusTranzactie.valueOf(cmd[2]);
                            updateStatus(raf, idx, status);
                            System.out.println("Updated [" + idx + "]: " + status);
                        }
                        case "PRINT_ALL" -> {
                            for (int i = 0; i < n; i++) {
                                System.out.println(readRecord(raf, i));
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeRecord(DataOutputStream out, int id, double suma, String data,
                                    TipTranzactie tip, StatusTranzactie status) throws Exception {
        out.write(toLittleEndianInt(id));
        out.write(toLittleEndianDouble(suma));

        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        byte[] fixedDate = new byte[10];
        int len = Math.min(dataBytes.length, 10);
        System.arraycopy(dataBytes, 0, fixedDate, 0, len);
        for (int i = len; i < 10; i++) {
            fixedDate[i] = ' ';
        }
        out.write(fixedDate);

        out.writeByte(tip == TipTranzactie.CREDIT ? 0 : 1);
        out.writeByte(status.getCode());

        out.write(new byte[8]);
    }

    private static byte[] toLittleEndianInt(int value) {
        return ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value)
                .array();
    }

    private static byte[] toLittleEndianDouble(double value) {
        return ByteBuffer.allocate(8)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putDouble(value)
                .array();
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws Exception {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] intBytes = new byte[4];
        byte[] doubleBytes = new byte[8];
        byte[] dateBytes = new byte[10];

        raf.readFully(intBytes);
        raf.readFully(doubleBytes);
        raf.readFully(dateBytes);

        int tipByte = raf.readUnsignedByte();
        byte statusByte = raf.readByte();

        raf.skipBytes(8);

        int id = ByteBuffer.wrap(intBytes)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getInt();

        double suma = ByteBuffer.wrap(doubleBytes)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getDouble();

        String data = new String(dateBytes, StandardCharsets.US_ASCII).trim();
        TipTranzactie tip = (tipByte == 0) ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        StatusTranzactie status = StatusTranzactie.fromByte(statusByte);

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }

    private static void updateStatus(RandomAccessFile raf, int idx, StatusTranzactie status) throws Exception {
        raf.seek((long) idx * RECORD_SIZE + 23);
        raf.writeByte(status.getCode());
    }
}