package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;
    private final int startId;

    public ATMThread(int atmId, CoadaTranzactii coada, int startId) {
        this.atmId = atmId;
        this.coada = coada;
        this.startId = startId;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 4; i++) {
                int tranzactieId = startId + i;
                double suma = 100 + tranzactieId * 10.0;
                String data = "2026-05-14";

                TranzactieSimpla t = new TranzactieSimpla(tranzactieId, suma, data);

                System.out.println("[ATM-" + atmId + "] trimite: " + t);
                coada.adauga(t, atmId);

                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}