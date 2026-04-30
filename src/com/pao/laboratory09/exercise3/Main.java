package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        try {
            CoadaTranzactii coada = new CoadaTranzactii(5);

            ATMThread atm1 = new ATMThread(1, coada, 1);
            ATMThread atm2 = new ATMThread(2, coada, 5);
            ATMThread atm3 = new ATMThread(3, coada, 9);

            ProcessorThread processorRunnable = new ProcessorThread(coada);
            Thread processor = new Thread(processorRunnable);

            atm1.start();
            atm2.start();
            atm3.start();
            processor.start();

            atm1.join();
            atm2.join();
            atm3.join();

            processorRunnable.activ = false;

            synchronized (coada) {
                coada.notifyAll();
            }

            processor.join();

            System.out.println("Toate tranzactiile procesate. Total: " + processorRunnable.getTotalProcesate());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}