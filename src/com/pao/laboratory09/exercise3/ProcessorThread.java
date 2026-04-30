package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {
    private final CoadaTranzactii coada;
    public volatile boolean activ = true;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }

    @Override
    public void run() {
        try {
            while (activ || !coada.esteGoala()) {
                TranzactieSimpla t;

                synchronized (coada) {
                    while (coada.esteGoala() && activ) {
                        coada.wait();
                    }

                    if (coada.esteGoala() && !activ) {
                        break;
                    }
                }

                t = coada.extrage();

                System.out.println("[Processor] Factura #" + t.getId() + " - "
                        + String.format("%.2f", t.getSuma()) + " RON | " + t.getData());

                totalProcesate++;
                Thread.sleep(80);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}