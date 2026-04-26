package com.pao.proiect.arenabid.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Auction {
    private int id;
    private AuctionItem item;
    private Seller seller;
    private AuctionStatus status;
    private List<Bid> bids;
    private Bidder winner;

    public Auction(int id, AuctionItem item, Seller seller) {
        this.id = id;
        this.item = item;
        this.seller = seller;
        this.status = AuctionStatus.OPEN;
        this.bids = new ArrayList<>();
        this.winner = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AuctionItem getItem() {
        return item;
    }

    public void setItem(AuctionItem item) {
        this.item = item;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Bidder getWinner() {
        return winner;
    }

    public void setWinner(Bidder winner) {
        this.winner = winner;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public double getCurrentPrice() {
        if (bids.isEmpty()) {
            return item.getStartingPrice();
        }
        return bids.get(bids.size() - 1).getAmount();
    }

    public Bid getHighestBid() {
        if (bids.isEmpty()) {
            return null;
        }

        Bid highestBid = bids.get(0);
        for (Bid bid : bids) {
            if (bid.getAmount() > highestBid.getAmount()) {
                highestBid = bid;
            }
        }
        return highestBid;
    }

    public void closeAuction() {
        this.status = AuctionStatus.CLOSED;
        Bid highestBid = getHighestBid();
        if (highestBid != null) {
            this.winner = highestBid.getBidder();
        }
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", item=" + item.getName() +
                ", seller=" + seller.getName() +
                ", status=" + status +
                ", currentPrice=" + getCurrentPrice() +
                ", winner=" + (winner != null ? winner.getName() : "none") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auction auction)) return false;
        return id == auction.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}