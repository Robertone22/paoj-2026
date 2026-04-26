package com.pao.proiect.arenabid.service;

import com.pao.proiect.arenabid.exception.EntityNotFoundException;
import com.pao.proiect.arenabid.model.Bidder;
import com.pao.proiect.arenabid.model.Seller;
import com.pao.proiect.arenabid.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;

    private final List<User> users;
    private final Map<Integer, User> usersById;

    private UserService() {
        this.users = new ArrayList<>();
        this.usersById = new HashMap<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
        usersById.put(user.getId(), user);
    }

    public void removeUser(int id) {
        User user = findById(id);
        users.remove(user);
        usersById.remove(id);
    }

    public User findById(int id) {
        User user = usersById.get(id);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " was not found.");
        }
        return user;
    }

    public List<User> findByName(String name) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<Seller> getAllSellers() {
        List<Seller> sellers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Seller seller) {
                sellers.add(seller);
            }
        }
        return sellers;
    }

    public List<Bidder> getAllBidders() {
        List<Bidder> bidders = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Bidder bidder) {
                bidders.add(bidder);
            }
        }
        return bidders;
    }
}