package com.pao.proiect.arenabid.model;

import java.util.Objects;

public class AuctionItem {
    private int id;
    private String name;
    private SportType sportType;
    private String athleteName;
    private RarityLevel rarityLevel;
    private ItemCondition itemCondition;
    private double startingPrice;
    private boolean premium;
    private AuthenticityCertificate certificate;

    public AuctionItem(int id, String name, SportType sportType, String athleteName,
                       RarityLevel rarityLevel, ItemCondition itemCondition,
                       double startingPrice, boolean premium,
                       AuthenticityCertificate certificate) {
        this.id = id;
        this.name = name;
        this.sportType = sportType;
        this.athleteName = athleteName;
        this.rarityLevel = rarityLevel;
        this.itemCondition = itemCondition;
        this.startingPrice = startingPrice;
        this.premium = premium;
        this.certificate = certificate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public RarityLevel getRarityLevel() {
        return rarityLevel;
    }

    public void setRarityLevel(RarityLevel rarityLevel) {
        this.rarityLevel = rarityLevel;
    }

    public ItemCondition getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(ItemCondition itemCondition) {
        this.itemCondition = itemCondition;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public AuthenticityCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(AuthenticityCertificate certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return "AuctionItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sportType=" + sportType +
                ", athleteName='" + athleteName + '\'' +
                ", rarityLevel=" + rarityLevel +
                ", itemCondition=" + itemCondition +
                ", startingPrice=" + startingPrice +
                ", premium=" + premium +
                ", certificate=" + certificate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuctionItem that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}