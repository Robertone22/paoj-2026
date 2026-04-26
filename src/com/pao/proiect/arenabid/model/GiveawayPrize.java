package com.pao.proiect.arenabid.model;

public class GiveawayPrize {
    private int id;
    private String name;
    private SportType sportType;
    private String description;

    public GiveawayPrize(int id, String name, SportType sportType, String description) {
        this.id = id;
        this.name = name;
        this.sportType = sportType;
        this.description = description;
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

    public SportType getSportType() {
        return sportType;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GiveawayPrize{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sportType=" + sportType +
                ", description='" + description + '\'' +
                '}';
    }
}