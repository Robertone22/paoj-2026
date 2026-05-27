package com.pao.laboratory14.exercise1;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collector;

public class RaportVanzariCollector {
    private RaportVanzariCollector() {
    }

    public static Collector<Bilet, ?, RaportVanzari> toRaportVanzari() {
        class Accumulator {
            private final Map<TipBilet, Long> numarPerTip = new EnumMap<>(TipBilet.class);
            private final Map<TipBilet, Double> incasariPerTip = new EnumMap<>(TipBilet.class);
            private double totalGlobal = 0.0;
            private long totalBilete = 0;

            private void add(Bilet bilet) {
                TipBilet tip = bilet.getTip();

                numarPerTip.merge(tip, 1L, Long::sum);
                incasariPerTip.merge(tip, bilet.getPret(), Double::sum);

                totalGlobal += bilet.getPret();
                totalBilete++;
            }

            private Accumulator combine(Accumulator other) {
                other.numarPerTip.forEach((tip, count) ->
                        numarPerTip.merge(tip, count, Long::sum)
                );

                other.incasariPerTip.forEach((tip, suma) ->
                        incasariPerTip.merge(tip, suma, Double::sum)
                );

                totalGlobal += other.totalGlobal;
                totalBilete += other.totalBilete;

                return this;
            }

            private RaportVanzari finish() {
                double medieGlobala = totalBilete == 0 ? 0.0 : totalGlobal / totalBilete;
                TipBilet tipCelMaiPopular = findTipCelMaiPopular();

                return new RaportVanzari(
                        numarPerTip,
                        incasariPerTip,
                        totalGlobal,
                        medieGlobala,
                        tipCelMaiPopular
                );
            }

            private TipBilet findTipCelMaiPopular() {
                TipBilet result = null;
                long bestCount = -1;

                for (TipBilet tip : TipBilet.values()) {
                    long count = numarPerTip.getOrDefault(tip, 0L);

                    if (count > bestCount) {
                        bestCount = count;
                        result = tip;
                    }
                }

                return result;
            }
        }

        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish
        );
    }
}