package com.pao.proiect.arenabid.service;

import com.pao.proiect.arenabid.exception.ClosedAuctionException;
import com.pao.proiect.arenabid.exception.EntityNotFoundException;
import com.pao.proiect.arenabid.exception.InvalidBidException;
import com.pao.proiect.arenabid.model.Auction;
import com.pao.proiect.arenabid.model.AuctionItem;
import com.pao.proiect.arenabid.model.AuctionStatus;
import com.pao.proiect.arenabid.model.Bid;
import com.pao.proiect.arenabid.model.Bidder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionService {
    private static AuctionService instance;

    private final List<AuctionItem> items;
    private final Map<Integer, AuctionItem> itemsById;
    private final List<Auction> auctions;
    private final Map<Integer, Auction> auctionsById;

    private AuctionService() {
        this.items = new ArrayList<>();
        this.itemsById = new HashMap<>();
        this.auctions = new ArrayList<>();
        this.auctionsById = new HashMap<>();
    }

    public static AuctionService getInstance() {
        if (instance == null) {
            instance = new AuctionService();
        }
        return instance;
    }

    public void addItem(AuctionItem item) {
        items.add(item);
        itemsById.put(item.getId(), item);
    }

    public List<AuctionItem> getAllItems() {
        return new ArrayList<>(items);
    }

    public List<AuctionItem> getPremiumItems() {
        List<AuctionItem> premiumItems = new ArrayList<>();
        for (AuctionItem item : items) {
            if (item.isPremium()) {
                premiumItems.add(item);
            }
        }
        return premiumItems;
    }

    public AuctionItem findItemById(int id) {
        AuctionItem item = itemsById.get(id);
        if (item == null) {
            throw new EntityNotFoundException("Auction item with id " + id + " was not found.");
        }
        return item;
    }

    public void createAuction(Auction auction) {
        auctions.add(auction);
        auctionsById.put(auction.getId(), auction);
    }

    public Auction findAuctionById(int id) {
        Auction auction = auctionsById.get(id);
        if (auction == null) {
            throw new EntityNotFoundException("Auction with id " + id + " was not found.");
        }
        return auction;
    }

    public List<Auction> getAllAuctions() {
        return new ArrayList<>(auctions);
    }

    public List<Auction> getOpenAuctions() {
        List<Auction> openAuctions = new ArrayList<>();
        for (Auction auction : auctions) {
            if (auction.getStatus() == AuctionStatus.OPEN) {
                openAuctions.add(auction);
            }
        }
        return openAuctions;
    }

    public void placeBid(int auctionId, Bid bid) {
        Auction auction = findAuctionById(auctionId);

        if (auction.getStatus() == AuctionStatus.CLOSED) {
            throw new ClosedAuctionException("Cannot place bid. Auction is already closed.");
        }

        if (bid.getAmount() <= auction.getCurrentPrice()) {
            throw new InvalidBidException("Bid amount must be greater than current price.");
        }

        auction.addBid(bid);
    }

    public List<Bid> getBidHistory(int auctionId) {
        Auction auction = findAuctionById(auctionId);
        List<Bid> sortedBids = new ArrayList<>(auction.getBids());
        sortedBids.sort(Comparator.comparingDouble(Bid::getAmount).reversed());
        return sortedBids;
    }

    public Bid closeAuction(int auctionId) {
        Auction auction = findAuctionById(auctionId);
        auction.closeAuction();

        Bid highestBid = auction.getHighestBid();
        if (highestBid != null) {
            Bidder winner = highestBid.getBidder();
            winner.incrementWonAuctionsCount();

            if (auction.getItem().isPremium()) {
                GiveawayService.getInstance().addParticipant(winner);
            }
        }

        return highestBid;
    }
}