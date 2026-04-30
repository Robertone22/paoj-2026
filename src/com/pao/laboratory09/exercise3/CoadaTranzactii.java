package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<TranzactieSimpla> coada = new LinkedList<>();
    private final int capacitateMaxima;

    public CoadaTranzactii(int capacitateMaxima) {
        this.capacitateMaxima = capacitateMaxima;
    }

    public synchronized void adauga(TranzactieSimpla t, int atmId) throws InterruptedException {
        while (coada.size() >= capacitateMaxima) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }

        coada.add(t);
        notifyAll();
    }

    public synchronized TranzactieSimpla extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }

        TranzactieSimpla t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean esteGoala() {
        return coada.isEmpty();
    }
}