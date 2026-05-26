package com.pao.proiect.arenabid;

import com.pao.proiect.arenabid.exception.ClosedAuctionException;
import com.pao.proiect.arenabid.exception.EntityNotFoundException;
import com.pao.proiect.arenabid.exception.InvalidBidException;
import com.pao.proiect.arenabid.model.*;
import com.pao.proiect.arenabid.service.AuctionService;
import com.pao.proiect.arenabid.service.GiveawayService;
import com.pao.proiect.arenabid.service.UserService;
import com.pao.proiect.arenabid.util.DatabaseInitializer;
import com.pao.proiect.arenabid.repository.UserRepository;
import com.pao.proiect.arenabid.repository.AuctionItemRepository;
import com.pao.proiect.arenabid.repository.AuctionRepository;
import com.pao.proiect.arenabid.repository.BidRepository;
import com.pao.proiect.arenabid.service.AuditService;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = UserService.getInstance();
    private static final AuctionService auctionService = AuctionService.getInstance();
    private static final GiveawayService giveawayService = GiveawayService.getInstance();
    private static final UserRepository userRepository = new UserRepository();
    private static final AuctionItemRepository auctionItemRepository = new AuctionItemRepository();
    private static final AuctionRepository auctionRepository = new AuctionRepository();
    private static final BidRepository bidRepository = new BidRepository();
    private static final AuditService auditService = AuditService.getInstance();

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
                    case 20 -> initializeDatabase();
                    case 21 -> saveUserToDatabase();
                    case 22 -> showUsersFromDatabase();
                    case 23 -> findUserInDatabaseById();
                    case 24 -> updateUserInDatabase();
                    case 25 -> deleteUserFromDatabase();
                    case 26 -> saveAuctionItemToDatabase();
                    case 27 -> showAuctionItemsFromDatabase();
                    case 28 -> findAuctionItemInDatabaseById();
                    case 29 -> updateAuctionItemInDatabase();
                    case 30 -> deleteAuctionItemFromDatabase();
                    case 31 -> saveAuctionToDatabase();
                    case 32 -> showAuctionsFromDatabase();
                    case 33 -> findAuctionInDatabaseById();
                    case 34 -> updateAuctionInDatabase();
                    case 35 -> deleteAuctionFromDatabase();
                    case 36 -> saveBidToDatabase();
                    case 37 -> showBidsFromDatabase();
                    case 38 -> findBidInDatabaseById();
                    case 39 -> showBidsForAuctionFromDatabase();
                    case 40 -> updateBidInDatabase();
                    case 41 -> deleteBidFromDatabase();
                    case 42 -> closeAuctionWithDatabaseTransaction();
                    case 43 -> showAuctionsWithSellerAndItem();
                    case 44 -> showBidsWithBidderAndItem();
                    case 45 -> showGiveawayEntriesWithWinnerAndItem();
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
        System.out.println("20. Initialize database schema");
        System.out.println("21. Save user to database");
        System.out.println("22. Show users from database");
        System.out.println("23. Find user in database by id");
        System.out.println("24. Update user in database");
        System.out.println("25. Delete user from database");
        System.out.println("26. Save auction item to database");
        System.out.println("27. Show auction items from database");
        System.out.println("28. Find auction item in database by id");
        System.out.println("29. Update auction item in database");
        System.out.println("30. Delete auction item from database");
        System.out.println("31. Save auction to database");
        System.out.println("32. Show auctions from database");
        System.out.println("33. Find auction in database by id");
        System.out.println("34. Update auction in database");
        System.out.println("35. Delete auction from database");
        System.out.println("36. Save bid to database");
        System.out.println("37. Show bids from database");
        System.out.println("38. Find bid in database by id");
        System.out.println("39. Show bids for auction from database");
        System.out.println("40. Update bid in database");
        System.out.println("41. Delete bid from database");
        System.out.println("42. Close auction with database transaction");
        System.out.println("43. Show auctions with seller and item");
        System.out.println("44. Show bids with bidder and item");
        System.out.println("45. Show giveaway entries with winner and item");
        System.out.println("0. Exit");
        System.out.println("==================================");
    }

    private static void addSeller() {
        auditService.logAction("add_seller");
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
        auditService.logAction("add_bidder");
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
        auditService.logAction("show_all_users");
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
        auditService.logAction("remove_user");
        System.out.print("User id to remove: ");
        int id = Integer.parseInt(scanner.nextLine());

        userService.removeUser(id);
        System.out.println("User removed successfully.");
    }

    private static void findUserById() {
        auditService.logAction("find_user_by_id");
        System.out.print("User id: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userService.findById(id);
        System.out.println(user);
    }

    private static void findUsersByName() {
        auditService.logAction("find_users_by_name");
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
        auditService.logAction("add_auction_item");
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
        auditService.logAction("show_all_auction_items");
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
        auditService.logAction("show_premium_auction_items");
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
        auditService.logAction("find_auction_item_by_id");
        System.out.print("Auction item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionService.findItemById(id);
        System.out.println(item);
    }

    private static void createAuction() {
        auditService.logAction("create_auction");
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
        auditService.logAction("show_all_auctions");
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
        auditService.logAction("show_open_auctions");
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
        auditService.logAction("find_auction_by_id");
        System.out.print("Auction id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Auction auction = auctionService.findAuctionById(id);
        System.out.println(auction);
    }

    private static void placeBid() {
        auditService.logAction("place_bid");
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
        auditService.logAction("show_bid_history");
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
        auditService.logAction("close_auction");
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

    private static void saveAuctionToDatabase() {
        auditService.logAction("save_auction_to_database");
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        System.out.print("Auction item id: ");
        int itemId = Integer.parseInt(scanner.nextLine());

        System.out.print("Seller id: ");
        int sellerId = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            System.out.println("No auction item found in database with this id.");
            return;
        }

        User user = userRepository.findById(sellerId).orElse(null);
        if (!(user instanceof Seller seller)) {
            System.out.println("No seller found in database with this id.");
            return;
        }

        Auction auction = new Auction(auctionId, item, seller);
        auctionRepository.save(auction);

        System.out.println("Auction saved to database successfully.");
    }

    private static void showAuctionsFromDatabase() {
        auditService.logAction("show_auctions_from_database");
        List<Auction> auctions = auctionRepository.findAll();

        if (auctions.isEmpty()) {
            System.out.println("No auctions found in database.");
            return;
        }

        for (Auction auction : auctions) {
            System.out.println(auction);
        }
    }

    private static void findAuctionInDatabaseById() {
        auditService.logAction("find_auction_in_database_by_id");
        System.out.print("Auction id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Auction auction = auctionRepository.findById(id).orElse(null);

        if (auction == null) {
            System.out.println("No auction found in database with this id.");
            return;
        }

        System.out.println(auction);
    }

    private static void updateAuctionInDatabase() {
        auditService.logAction("update_auction_in_database");
        System.out.print("Auction id to update: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        Auction existingAuction = auctionRepository.findById(auctionId).orElse(null);

        if (existingAuction == null) {
            System.out.println("No auction found in database with this id.");
            return;
        }

        System.out.print("New auction item id: ");
        int itemId = Integer.parseInt(scanner.nextLine());

        System.out.print("New seller id: ");
        int sellerId = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            System.out.println("No auction item found in database with this id.");
            return;
        }

        User user = userRepository.findById(sellerId).orElse(null);
        if (!(user instanceof Seller seller)) {
            System.out.println("No seller found in database with this id.");
            return;
        }

        Auction updatedAuction = new Auction(auctionId, item, seller);
        updatedAuction.setStatus(existingAuction.getStatus());
        updatedAuction.setWinner(existingAuction.getWinner());

        auctionRepository.update(updatedAuction);

        System.out.println("Auction updated in database successfully.");
    }


    private static void deleteAuctionFromDatabase() {
        auditService.logAction("delete_auction_from_database");
        System.out.print("Auction id to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        auctionRepository.delete(id);
        System.out.println("Auction deleted from database successfully.");
    }

    private static void initializeDatabase() {
        auditService.logAction("initialize_database");
        DatabaseInitializer.initializeDatabase();
    }

    private static void saveUserToDatabase() {
        auditService.logAction("save_user_to_database");
        System.out.print("User id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("User name: ");
        String name = scanner.nextLine();

        System.out.print("User email: ");
        String email = scanner.nextLine();

        System.out.print("Role (SELLER/BIDDER): ");
        String role = scanner.nextLine().trim().toUpperCase();

        User user;

        if (role.equals("SELLER")) {
            user = new Seller(id, name, email);
        } else if (role.equals("BIDDER")) {
            user = new Bidder(id, name, email);
        } else {
            System.out.println("Invalid role. User was not saved.");
            return;
        }

        userRepository.save(user);
        System.out.println("User saved to database successfully.");
    }

    private static void showUsersFromDatabase() {
        auditService.logAction("show_users_from_database");
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            System.out.println("No users found in database.");
            return;
        }

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void findUserInDatabaseById() {
        auditService.logAction("find_user_in_database_by_id");
        System.out.print("User id: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            System.out.println("No user found in database with this id.");
            return;
        }

        System.out.println(user);
    }

    private static void updateUserInDatabase() {
        auditService.logAction("update_user_in_database");
        System.out.print("User id to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            System.out.println("No user found in database with this id.");
            return;
        }

        System.out.print("New name: ");
        String name = scanner.nextLine();

        System.out.print("New email: ");
        String email = scanner.nextLine();

        User updatedUser;

        if (existingUser instanceof Seller) {
            updatedUser = new Seller(id, name, email);
        } else {
            Bidder bidder = new Bidder(id, name, email);

            if (existingUser instanceof Bidder existingBidder) {
                bidder.setWonAuctionsCount(existingBidder.getWonAuctionsCount());
            }

            updatedUser = bidder;
        }

        userRepository.update(updatedUser);
        System.out.println("User updated in database successfully.");
    }

    private static void deleteUserFromDatabase() {
        auditService.logAction("delete_user_from_database");
        System.out.print("User id to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        userRepository.delete(id);
        System.out.println("User deleted from database successfully.");
    }

    private static void saveAuctionItemToDatabase() {
        auditService.logAction("save_auction_item_to_database");
        AuctionItem item = readAuctionItemFromConsole();
        auctionItemRepository.save(item);
        System.out.println("Auction item saved to database successfully.");
    }

    private static void showAuctionItemsFromDatabase() {
        auditService.logAction("show_auction_items_from_database");
        List<AuctionItem> items = auctionItemRepository.findAll();

        if (items.isEmpty()) {
            System.out.println("No auction items found in database.");
            return;
        }

        for (AuctionItem item : items) {
            System.out.println(item);
        }
    }

    private static void findAuctionItemInDatabaseById() {
        auditService.logAction("find_auction_item_in_database_by_id");
        System.out.print("Auction item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        AuctionItem item = auctionItemRepository.findById(id).orElse(null);

        if (item == null) {
            System.out.println("No auction item found in database with this id.");
            return;
        }

        System.out.println(item);
    }

    private static void updateAuctionItemInDatabase() {
        auditService.logAction("update_auction_item_in_database");
        System.out.print("Auction item id to update: ");
        int existingId = Integer.parseInt(scanner.nextLine());

        AuctionItem existingItem = auctionItemRepository.findById(existingId).orElse(null);

        if (existingItem == null) {
            System.out.println("No auction item found in database with this id.");
            return;
        }

        System.out.println("Enter the new data for this auction item.");

        AuctionItem updatedItem = readAuctionItemFromConsoleWithFixedId(existingId);
        auctionItemRepository.update(updatedItem);

        System.out.println("Auction item updated in database successfully.");
    }

    private static void deleteAuctionItemFromDatabase() {
        auditService.logAction("delete_auction_item_from_database");
        System.out.print("Auction item id to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        auctionItemRepository.delete(id);
        System.out.println("Auction item deleted from database successfully.");
    }

    private static AuctionItem readAuctionItemFromConsole() {
        System.out.print("Item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        return readAuctionItemFromConsoleWithFixedId(id);
    }

    private static AuctionItem readAuctionItemFromConsoleWithFixedId(int id) {
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

        return new AuctionItem(
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
    }

    private static void saveBidToDatabase() {
        auditService.logAction("save_bid_to_database");
        System.out.print("Bid id: ");
        int bidId = Integer.parseInt(scanner.nextLine());

        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        System.out.print("Bidder id: ");
        int bidderId = Integer.parseInt(scanner.nextLine());

        System.out.print("Bid amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        Auction auction = auctionRepository.findById(auctionId).orElse(null);
        if (auction == null) {
            System.out.println("No auction found in database with this id.");
            return;
        }

        User user = userRepository.findById(bidderId).orElse(null);
        if (!(user instanceof Bidder bidder)) {
            System.out.println("No bidder found in database with this id.");
            return;
        }

        if (amount <= auction.getCurrentPrice()) {
            System.out.println("Bid amount must be greater than current auction price.");
            return;
        }

        Bid bid = new Bid(bidId, bidder, amount, LocalDateTime.now());
        bidRepository.saveForAuction(auctionId, bid);

        System.out.println("Bid saved to database successfully.");
    }

    private static void showBidsFromDatabase() {
        auditService.logAction("show_bids_from_database");
        List<Bid> bids = bidRepository.findAll();

        if (bids.isEmpty()) {
            System.out.println("No bids found in database.");
            return;
        }

        for (Bid bid : bids) {
            System.out.println(bid);
        }
    }

    private static void findBidInDatabaseById() {
        auditService.logAction("find_bid_in_database_by_id");
        System.out.print("Bid id: ");
        int bidId = Integer.parseInt(scanner.nextLine());

        Bid bid = bidRepository.findById(bidId).orElse(null);

        if (bid == null) {
            System.out.println("No bid found in database with this id.");
            return;
        }

        System.out.println(bid);
    }

    private static void showBidsForAuctionFromDatabase() {
        auditService.logAction("show_bids_for_auction_from_database");
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        List<Bid> bids = bidRepository.findByAuctionId(auctionId);

        if (bids.isEmpty()) {
            System.out.println("No bids found for this auction in database.");
            return;
        }

        for (Bid bid : bids) {
            System.out.println(bid);
        }
    }

    private static void updateBidInDatabase() {
        auditService.logAction("update_bid_in_database");
        System.out.print("Bid id to update: ");
        int bidId = Integer.parseInt(scanner.nextLine());

        Bid existingBid = bidRepository.findById(bidId).orElse(null);

        if (existingBid == null) {
            System.out.println("No bid found in database with this id.");
            return;
        }

        System.out.print("New bidder id: ");
        int bidderId = Integer.parseInt(scanner.nextLine());

        System.out.print("New amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        User user = userRepository.findById(bidderId).orElse(null);
        if (!(user instanceof Bidder bidder)) {
            System.out.println("No bidder found in database with this id.");
            return;
        }

        Bid updatedBid = new Bid(bidId, bidder, amount, LocalDateTime.now());
        bidRepository.update(updatedBid);

        System.out.println("Bid updated in database successfully.");
    }

    private static void deleteBidFromDatabase() {
        auditService.logAction("delete_bid_from_database");
        System.out.print("Bid id to delete: ");
        int bidId = Integer.parseInt(scanner.nextLine());

        bidRepository.delete(bidId);
        System.out.println("Bid deleted from database successfully.");
    }

    private static void showGiveawayParticipants() {
        auditService.logAction("show_giveaway_participants");
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
        auditService.logAction("start_giveaway");
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

    private static void closeAuctionWithDatabaseTransaction() {
        auditService.logAction("close_auction_with_database_transaction");
        System.out.print("Auction id: ");
        int auctionId = Integer.parseInt(scanner.nextLine());

        auctionRepository.closeAuctionWithTransaction(auctionId);

        System.out.println("Auction closed using JDBC transaction.");
    }

    private static void showAuctionsWithSellerAndItem() {
        auditService.logAction("show_auctions_with_seller_and_item");
        List<String> rows = auctionRepository.findAuctionsWithSellerAndItem();

        if (rows.isEmpty()) {
            System.out.println("No auction join results found.");
            return;
        }

        for (String row : rows) {
            System.out.println(row);
        }
    }

    private static void showBidsWithBidderAndItem() {
        auditService.logAction("show_bids_with_bidder_and_item");
        List<String> rows = auctionRepository.findBidsWithBidderAndItem();

        if (rows.isEmpty()) {
            System.out.println("No bid join results found.");
            return;
        }

        for (String row : rows) {
            System.out.println(row);
        }
    }

    private static void showGiveawayEntriesWithWinnerAndItem() {
        auditService.logAction("show_giveaway_entries_with_winner_and_item");
        List<String> rows = auctionRepository.findGiveawayEntriesWithWinnerAndAuctionItem();

        if (rows.isEmpty()) {
            System.out.println("No giveaway join results found.");
            return;
        }

        for (String row : rows) {
            System.out.println(row);
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

