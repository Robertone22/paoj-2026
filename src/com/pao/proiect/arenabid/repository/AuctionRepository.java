package com.pao.proiect.arenabid.repository;

import com.pao.proiect.arenabid.model.Auction;
import com.pao.proiect.arenabid.model.AuctionItem;
import com.pao.proiect.arenabid.model.AuctionStatus;
import com.pao.proiect.arenabid.model.Bidder;
import com.pao.proiect.arenabid.model.Seller;
import com.pao.proiect.arenabid.model.User;
import com.pao.proiect.arenabid.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Collections;

public class AuctionRepository implements Repository<Auction, Integer> {
    private final Connection connection;
    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;

    public AuctionRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.auctionItemRepository = new AuctionItemRepository();
        this.userRepository = new UserRepository();
    }

    @Override
    public void save(Auction auction) {
        String sql = """
                INSERT INTO auctions (id, item_id, seller_id, status, winner_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, auction.getId());
            preparedStatement.setInt(2, auction.getItem().getId());
            preparedStatement.setInt(3, auction.getSeller().getId());
            preparedStatement.setString(4, auction.getStatus().name());

            if (auction.getWinner() != null) {
                preparedStatement.setInt(5, auction.getWinner().getId());
            } else {
                preparedStatement.setObject(5, null);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save auction.", e);
        }
    }

    @Override
    public Optional<Auction> findById(Integer id) {
        String sql = """
                SELECT id, item_id, seller_id, status, winner_id
                FROM auctions
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToAuction(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find auction by id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Auction> findAll() {
        String sql = """
                SELECT id, item_id, seller_id, status, winner_id
                FROM auctions
                """;

        List<Auction> auctions = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                auctions.add(mapResultSetToAuction(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find all auctions.", e);
        }

        return auctions;
    }

    @Override
    public void update(Auction auction) {
        String sql = """
                UPDATE auctions
                SET item_id = ?, seller_id = ?, status = ?, winner_id = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, auction.getItem().getId());
            preparedStatement.setInt(2, auction.getSeller().getId());
            preparedStatement.setString(3, auction.getStatus().name());

            if (auction.getWinner() != null) {
                preparedStatement.setInt(4, auction.getWinner().getId());
            } else {
                preparedStatement.setObject(4, null);
            }

            preparedStatement.setInt(5, auction.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update auction.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM auctions
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete auction.", e);
        }
    }

    public void closeAuctionWithTransaction(Integer auctionId) {
        String findAuctionSql = """
            SELECT a.status, ai.premium
            FROM auctions a
            JOIN auction_items ai ON a.item_id = ai.id
            WHERE a.id = ?
            """;

        String findHighestBidSql = """
            SELECT bidder_id
            FROM bids
            WHERE auction_id = ?
            ORDER BY amount DESC
            LIMIT 1
            """;

        String closeAuctionSql = """
            UPDATE auctions
            SET status = 'CLOSED', winner_id = ?
            WHERE id = ?
            """;

        String incrementWinnerSql = """
            UPDATE users
            SET won_auctions_count = won_auctions_count + 1
            WHERE id = ?
            """;

        String insertGiveawayEntrySql = """
            INSERT INTO giveaway_entries (bidder_id, auction_id, entry_timestamp)
            VALUES (?, ?, ?)
            """;

        try {
            connection.setAutoCommit(false);

            boolean premium;
            String status;

            try (PreparedStatement preparedStatement = connection.prepareStatement(findAuctionSql)) {
                preparedStatement.setInt(1, auctionId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException("Auction not found.");
                    }

                    status = resultSet.getString("status");
                    premium = resultSet.getInt("premium") == 1;
                }
            }

            if ("CLOSED".equalsIgnoreCase(status)) {
                throw new SQLException("Auction is already closed.");
            }

            Integer winnerId = null;

            try (PreparedStatement preparedStatement = connection.prepareStatement(findHighestBidSql)) {
                preparedStatement.setInt(1, auctionId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        winnerId = resultSet.getInt("bidder_id");
                    }
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(closeAuctionSql)) {
                if (winnerId != null) {
                    preparedStatement.setInt(1, winnerId);
                } else {
                    preparedStatement.setObject(1, null);
                }

                preparedStatement.setInt(2, auctionId);
                preparedStatement.executeUpdate();
            }

            if (winnerId != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(incrementWinnerSql)) {
                    preparedStatement.setInt(1, winnerId);
                    preparedStatement.executeUpdate();
                }

                if (premium) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertGiveawayEntrySql)) {
                        preparedStatement.setInt(1, winnerId);
                        preparedStatement.setInt(2, auctionId);
                        preparedStatement.setString(3, LocalDateTime.now().toString());
                        preparedStatement.executeUpdate();
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException("Transaction failed and rollback also failed.", rollbackException);
            }

            throw new RuntimeException("Could not close auction using transaction.", e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Could not reset auto commit.", e);
            }
        }
    }

    public List<String> findAuctionsWithSellerAndItem() {
        String sql = """
            SELECT a.id AS auction_id,
                   ai.name AS item_name,
                   u.name AS seller_name,
                   a.status AS auction_status,
                   ai.starting_price AS starting_price,
                   a.winner_id AS winner_id
            FROM auctions a
            JOIN auction_items ai ON a.item_id = ai.id
            JOIN users u ON a.seller_id = u.id
            ORDER BY a.id
            """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String line = "Auction id=" + resultSet.getInt("auction_id") +
                        ", item='" + resultSet.getString("item_name") + '\'' +
                        ", seller='" + resultSet.getString("seller_name") + '\'' +
                        ", status=" + resultSet.getString("auction_status") +
                        ", startingPrice=" + resultSet.getDouble("starting_price") +
                        ", winnerId=" + resultSet.getObject("winner_id");

                result.add(line);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not run join query for auctions with seller and item.", e);
        }

        return result;
    }

    public List<String> findBidsWithBidderAndItem() {
        String sql = """
            SELECT b.id AS bid_id,
                   b.amount AS amount,
                   b.timestamp AS bid_timestamp,
                   u.name AS bidder_name,
                   ai.name AS item_name,
                   a.id AS auction_id
            FROM bids b
            JOIN users u ON b.bidder_id = u.id
            JOIN auctions a ON b.auction_id = a.id
            JOIN auction_items ai ON a.item_id = ai.id
            ORDER BY b.amount DESC
            """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String line = "Bid id=" + resultSet.getInt("bid_id") +
                        ", auctionId=" + resultSet.getInt("auction_id") +
                        ", bidder='" + resultSet.getString("bidder_name") + '\'' +
                        ", item='" + resultSet.getString("item_name") + '\'' +
                        ", amount=" + resultSet.getDouble("amount") +
                        ", timestamp=" + resultSet.getString("bid_timestamp");

                result.add(line);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not run join query for bids with bidder and item.", e);
        }

        return result;
    }

    public List<String> findGiveawayEntriesWithWinnerAndAuctionItem() {
        String sql = """
            SELECT ge.id AS entry_id,
                   u.name AS winner_name,
                   ai.name AS item_name,
                   a.id AS auction_id,
                   ge.entry_timestamp AS entry_timestamp
            FROM giveaway_entries ge
            JOIN users u ON ge.bidder_id = u.id
            JOIN auctions a ON ge.auction_id = a.id
            JOIN auction_items ai ON a.item_id = ai.id
            ORDER BY ge.entry_timestamp DESC
            """;

        List<String> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String line = "Giveaway entry id=" + resultSet.getInt("entry_id") +
                        ", winner='" + resultSet.getString("winner_name") + '\'' +
                        ", auctionId=" + resultSet.getInt("auction_id") +
                        ", item='" + resultSet.getString("item_name") + '\'' +
                        ", entryTimestamp=" + resultSet.getString("entry_timestamp");

                result.add(line);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not run join query for giveaway entries.", e);
        }

        return result;
    }

    private Auction mapResultSetToAuction(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int itemId = resultSet.getInt("item_id");
        int sellerId = resultSet.getInt("seller_id");
        String status = resultSet.getString("status");

        AuctionItem item = auctionItemRepository.findById(itemId)
                .orElseThrow(() -> new SQLException("Auction item not found for id: " + itemId));

        User sellerUser = userRepository.findById(sellerId)
                .orElseThrow(() -> new SQLException("Seller not found for id: " + sellerId));

        if (!(sellerUser instanceof Seller seller)) {
            throw new SQLException("User with id " + sellerId + " is not a seller.");
        }

        Auction auction = new Auction(id, item, seller);
        auction.setStatus(AuctionStatus.valueOf(status));

        int winnerId = resultSet.getInt("winner_id");
        if (!resultSet.wasNull()) {
            User winnerUser = userRepository.findById(winnerId)
                    .orElseThrow(() -> new SQLException("Winner not found for id: " + winnerId));

            if (winnerUser instanceof Bidder bidder) {
                auction.setWinner(bidder);
            }
        }

        return auction;
    }
}