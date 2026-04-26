package com.pao.proiect.arenabid.service;

import com.pao.proiect.arenabid.model.Bidder;
import com.pao.proiect.arenabid.model.Giveaway;
import com.pao.proiect.arenabid.model.GiveawayPrize;
import com.pao.proiect.arenabid.model.GiveawayWinner;
import com.pao.proiect.arenabid.model.SportType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiveawayService {
    private static GiveawayService instance;

    private final Giveaway currentGiveaway;
    private final Random random;

    private GiveawayService() {
        this.random = new Random();

        List<GiveawayPrize> prizes = new ArrayList<>();
        prizes.add(new GiveawayPrize(1, "Signed Messi Ball", SportType.FOOTBALL, "Official football signed by Lionel Messi"));
        prizes.add(new GiveawayPrize(2, "Signed Nadal Cap", SportType.TENNIS, "Cap signed by Rafael Nadal"));
        prizes.add(new GiveawayPrize(3, "Hagi Collectible Poster", SportType.FOOTBALL, "Limited poster signed by Gheorghe Hagi"));
        prizes.add(new GiveawayPrize(4, "Signed Ferrari Cap", SportType.FORMULA_ONE, "Ferrari cap signed by a legendary driver"));
        prizes.add(new GiveawayPrize(5, "Signed Basketball Jersey", SportType.BASKETBALL, "Collector jersey signed by a basketball star"));
        prizes.add(new GiveawayPrize(6, "Mystery Legendary Sports Item", SportType.FOOTBALL, "Special rotating premium prize"));

        this.currentGiveaway = new Giveaway(1, prizes, 10);
    }

    public static GiveawayService getInstance() {
        if (instance == null) {
            instance = new GiveawayService();
        }
        return instance;
    }

    public void addParticipant(Bidder bidder) {
        currentGiveaway.addParticipant(bidder);
    }

    public List<Bidder> getParticipants() {
        return new ArrayList<>(currentGiveaway.getParticipants());
    }

    public Giveaway getCurrentGiveaway() {
        return currentGiveaway;
    }

    public GiveawayWinner startGiveawayIfReady() {
        if (!currentGiveaway.canStart()) {
            System.out.println("Giveaway cannot start yet. Current participants: "
                    + currentGiveaway.getParticipants().size() + "/" + currentGiveaway.getMinimumParticipants());
            return null;
        }

        int winnerIndex = random.nextInt(currentGiveaway.getParticipants().size());
        Bidder winner = currentGiveaway.getParticipants().get(winnerIndex);

        int prizeIndex = random.nextInt(currentGiveaway.getAvailablePrizes().size());
        GiveawayPrize prize = currentGiveaway.getAvailablePrizes().get(prizeIndex);

        GiveawayWinner giveawayWinner = new GiveawayWinner(winner, prize, LocalDateTime.now());

        System.out.println("Giveaway winner: " + winner.getName() + " won " + prize.getName());

        currentGiveaway.resetParticipants();
        return giveawayWinner;
    }
}