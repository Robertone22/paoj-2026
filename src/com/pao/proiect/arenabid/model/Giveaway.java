package com.pao.proiect.arenabid.model;

import java.util.ArrayList;
import java.util.List;

public class Giveaway {
    private int id;
    private List<Bidder> participants;
    private List<GiveawayPrize> availablePrizes;
    private int minimumParticipants;

    public Giveaway(int id, List<GiveawayPrize> availablePrizes, int minimumParticipants) {
        this.id = id;
        this.availablePrizes = availablePrizes;
        this.minimumParticipants = minimumParticipants;
        this.participants = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Bidder> getParticipants() {
        return participants;
    }

    public List<GiveawayPrize> getAvailablePrizes() {
        return availablePrizes;
    }

    public int getMinimumParticipants() {
        return minimumParticipants;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParticipants(List<Bidder> participants) {
        this.participants = participants;
    }

    public void setAvailablePrizes(List<GiveawayPrize> availablePrizes) {
        this.availablePrizes = availablePrizes;
    }

    public void setMinimumParticipants(int minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    public void addParticipant(Bidder bidder) {
        participants.add(bidder);
    }

    public boolean canStart() {
        return participants.size() >= minimumParticipants;
    }

    public void resetParticipants() {
        participants.clear();
    }

    @Override
    public String toString() {
        return "Giveaway{" +
                "id=" + id +
                ", participantsCount=" + participants.size() +
                ", minimumParticipants=" + minimumParticipants +
                ", availablePrizes=" + availablePrizes +
                '}';
    }
}