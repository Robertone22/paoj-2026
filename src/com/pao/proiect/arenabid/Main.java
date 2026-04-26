package com.pao.proiect.arenabid;

import com.pao.proiect.arenabid.exception.ClosedAuctionException;
import com.pao.proiect.arenabid.exception.EntityNotFoundException;
import com.pao.proiect.arenabid.exception.InvalidBidException;
import com.pao.proiect.arenabid.model.*;
import com.pao.proiect.arenabid.service.AuctionService;
import com.pao.proiect.arenabid.service.GiveawayService;
import com.pao.proiect.arenabid.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = UserService.getInstance();
    private static final AuctionService auctionService = AuctionService.getInstance();
    private static final GiveawayService giveawayService = GiveawayService.getInstance();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();

            try {
                System.out.print("Choose an option: ");
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> addSeller();
                    case 2 -> addBidder();
                    case 3 -> showAllUsers();
                    case 4 -> removeUser();
                    case 5 -> findUserById();
                    case 6 -> findUsersByName();
                    case 7 -> addAuctionItem();
                    case 8 -> showAllAuctionItems();
                    case 9 -> showPremiumAuctionItems();
                    case 10 -> findAuctionItemById();
                    case 11 -> createAuction();
                    case 12 -> showAllAuctions();
                    case 13 -> showOpenAuctions();
                    case 14 -> findAuctionById();
                    case 15 -> placeBid();
                    case 16 -> showBidHistory();
                    case 17 -> closeAuction();
                    case 18 -> showGiveawayParticipants();
                    case 19 -> startGiveaway();
                    case 0 -> {
                        running = false;
                        System.out.println("Exiting ArenaBid... Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }

            } catch (InvalidBidException | ClosedAuctionException | EntityNotFoundException e) {
                System.out.println("Application error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("========== ARENABID MENU ==========");
        System.out.println("1. Add new seller");
        System.out.println("2. Add new bidder");
        System.out.println("3. Show all users");
        System.out.println("4. Remove user");
        System.out.println("5. Find user by id");
        System.out.println("6. Find users by name");
        System.out.println("7. Add auction item");
        System.out.println("8. Show all auction items");
        System.out.println("9. Show premium auction items");
        System.out.println("10. Find auction item by id");
        System.out.println("11. Create auction");
        System.out.println("12. Show all auctions");
        System.out.println("13. Show open auctions");
        System.out.println("14. Find auction by id");
        System.out.println("15. Place bid");
        System.out.println("16. Show bid history");
        System.out.println("17. Close auction");
        System.out.println("18. Show giveaway participants");
        System.out.println("19. Start giveaway if ready");
        System.out.println("0. Exit");
        System.out.println("==================================");
    }

    private static void addSeller() {
        System.out.print("Seller id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Seller name: ");
        String name = scanner.nextLine();

        System.out.print("Seller email: ");
        String email = scanner.nextLine();

        Seller seller = new Seller(id, name, email);
        userService.addUser(seller);

        System.out.println("Seller added successfully.");
    }

    private static void addBidder() {
        System.out.print("Bidder id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Bidder name: ");
        String name = scanner.nextLine();

        System.out.print("Bidder email: ");
        String email = scanner.nextLine();

        Bidder bidder = new Bidder(id, name, email);
        userService.addUser(bidder);

        System.out.println("Bidder added successfully.");
    }

    private static void showAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void removeUser() {
        System.out.print("User id to remove: ");
        int id = Integer.parseInt(scanner.nextLine());

        userService.removeUser(id);
        System.out.println("User removed successfully.");
    }

    private static void findUserById() {
        System.out.print("User id: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userService.findById(id);
        System.out.println(user);
    }

    private static void findUsersByName() {
        System.out.print("User name: ");
        String name = scanner.nextLine();

        List<User> users = userService.findByName(name);

        if (users.isEmpty()) {
            System.out.println("No users found with this name.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void addAuctionItem() {
        System.out.print("Item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Item name: ");
        String name = scanner.nextLine();

        SportType sportType = readSportType();

        System.out.print("Athlete name: ");
        String athleteName = scanner.nextLine();

        RarityLevel rarityLevel = readRarityLevel();
        ItemCondition itemCondition = readItemCondition();

        System.out.print("Starting price: ");
        double startingPrice = Double.parseDouble(scanner.nextLine());

        System.out.print("Is premium? (true/false): ");
        boolean premium = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Does it have authenticity certificate? (true/false): ");
        boolean hasCertificate = Boolean.parseBoolean(scanner.nextLine());

        AuthenticityCertificate certificate = null;

        if (hasCertificate) {
            System.out.print("Certificate id: ");
            String certificateId = scanner.nextLine();

            System.out.print("Issued by: ");
            String issuedBy = scanner.nextLine();

            System.out.print("Issue year: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Issue month: ");
            int month = Integer.parseInt(scanner.nextLine());

            System.out.print("Issue day: ");
            int day = Integer.parseInt(scanner.nextLine());

            System.out.print("Is verified? (true/false): ");
            boolean verified = Boolean.parseBoolean(scanner.nextLine());

            certificate = new AuthenticityCertificate(
                    certificateId,
                    issuedBy,
                    LocalDate.of(year, month, day),
                    verified
            );
        }

        AuctionItem item = new AuctionItem(
                id,
                name,
                sportType,
                athleteName,
                rarityLevel,
                itemCondition,
                startingPrice,
                premium,
                certificate
        );

        auctionService.addItem(item);
        System.out.println("Auction item added successfully.");
    }

    private static void showAllAuctionItems() {
        List<AuctionItem> items = auctionService.getAllItems();

        if (items.isEmpty()) {
            System.out.println("No auction items found.");
            return;
        }

        for (AuctionItem item : items) {
            System.out.println(item);
        }
    }

    private static void showPremiumAuctionItems() {
        List<AuctionItem> items = auctionService.getPremiumItems();

        if (items.isEmpty()) {
            System.out.println("No premium auction items found.");
            return;
        }

        for (AuctionItem item : items) {
            System.out.println(item);
        }
    }

    private static void findAuctionItemById() {
        System.out.print("Auction item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionService.findItemById(id);
        System.out.println(item);
    }

    private static void createAuction() {
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        System.out.print("Auction item id: ");
        int itemId = Integer.parseInt(scanner.nextLine());

        System.out.print("Seller id: ");
        int sellerId = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionService.findItemById(itemId);
        User user = userService.findById(sellerId);

        if (!(user instanceof Seller seller)) {
            System.out.println("The provided user is not a seller.");
            return;
        }

        Auction auction = new Auction(auctionId, item, seller);
        auctionService.createAuction(auction);

        System.out.println("Auction created successfully.");
    }

    private static void showAllAuctions() {
        List<Auction> auctions = auctionService.getAllAuctions();

        if (auctions.isEmpty()) {
            System.out.println("No auctions found.");
            return;
        }

        for (Auction auction : auctions) {
            System.out.println(auction);
        }
    }

    private static void showOpenAuctions() {
        List<Auction> auctions = auctionService.getOpenAuctions();

        if (auctions.isEmpty()) {
            System.out.println("No open auctions found.");
            return;
        }

        for (Auction auction : auctions) {
            System.out.println(auction);
        }
    }

    private static void findAuctionById() {
        System.out.print("Auction id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Auction auction = auctionService.findAuctionById(id);
        System.out.println(auction);
    }

    private static void placeBid() {
        System.out.print("Bid id: ");
        int bidId = Integer.parseInt(scanner.nextLine());

        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        System.out.print("Bidder id: ");
        int bidderId = Integer.parseInt(scanner.nextLine());

        System.out.print("Bid amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        User user = userService.findById(bidderId);

        if (!(user instanceof Bidder bidder)) {
            System.out.println("The provided user is not a bidder.");
            return;
        }

        Bid bid = new Bid(bidId, bidder, amount, LocalDateTime.now());
        auctionService.placeBid(auctionId, bid);

        System.out.println("Bid placed successfully.");
    }

    private static void showBidHistory() {
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        List<Bid> bids = auctionService.getBidHistory(auctionId);

        if (bids.isEmpty()) {
            System.out.println("No bids found for this auction.");
            return;
        }

        for (Bid bid : bids) {
            System.out.println(bid);
        }
    }

    private static void closeAuction() {
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        Bid winningBid = auctionService.closeAuction(auctionId);

        if (winningBid == null) {
            System.out.println("Auction closed without any bids.");
            return;
        }

        System.out.println("Auction closed successfully.");
        System.out.println("Winner: " + winningBid.getBidder().getName());
        System.out.println("Winning amount: " + winningBid.getAmount());

        Auction auction = auctionService.findAuctionById(auctionId);
        if (auction.getItem().isPremium()) {
            System.out.println("This was a premium auction. Winner added to giveaway.");
        }
    }

    private static void showGiveawayParticipants() {
        List<Bidder> participants = giveawayService.getParticipants();

        if (participants.isEmpty()) {
            System.out.println("No giveaway participants yet.");
            return;
        }

        for (Bidder bidder : participants) {
            System.out.println(bidder.getName());
        }

        System.out.println("Current participants: " + participants.size() + "/"
                + giveawayService.getCurrentGiveaway().getMinimumParticipants());
    }

    private static void startGiveaway() {
        GiveawayWinner giveawayWinner = giveawayService.startGiveawayIfReady();

        if (giveawayWinner != null) {
            System.out.println("Official giveaway result:");
            System.out.println(giveawayWinner);
        }
    }

    private static SportType readSportType() {
        System.out.println("Choose sport type:");
        for (SportType value : SportType.values()) {
            System.out.println("- " + value);
        }

        while (true) {
            try {
                return SportType.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid sport type. Try again:");
            }
        }
    }

    private static RarityLevel readRarityLevel() {
        System.out.println("Choose rarity level:");
        for (RarityLevel value : RarityLevel.values()) {
            System.out.println("- " + value);
        }

        while (true) {
            try {
                return RarityLevel.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid rarity level. Try again:");
            }
        }
    }

    private static ItemCondition readItemCondition() {
        System.out.println("Choose item condition:");
        for (ItemCondition value : ItemCondition.values()) {
            System.out.println("- " + value);
        }

        while (true) {
            try {
                return ItemCondition.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid item condition. Try again:");
            }
        }
    }
}