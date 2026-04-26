package com.pao.proiect.arenabid.model;

public class Seller extends Participant {

    public Seller(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "SELLER";
    }
}