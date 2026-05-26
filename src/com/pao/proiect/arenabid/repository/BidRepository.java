package com.pao.proiect.arenabid.repository;

import com.pao.proiect.arenabid.model.Bid;
import com.pao.proiect.arenabid.model.Bidder;
import com.pao.proiect.arenabid.model.User;
import com.pao.proiect.arenabid.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BidRepository implements Repository<Bid, Integer> {
    private final Connection connection;
    private final UserRepository userRepository;

    public BidRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userRepository = new UserRepository();
    }

    public void saveForAuction(Integer auctionId, Bid bid) {
        String sql = """
                INSERT INTO bids (id, auction_id, bidder_id, amount, timestamp)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bid.getId());
            preparedStatement.setInt(2, auctionId);
            preparedStatement.setInt(3, bid.getBidder().getId());
            preparedStatement.setDouble(4, bid.getAmount());
            preparedStatement.setString(5, bid.getTimestamp().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save bid.", e);
        }
    }

    @Override
    public void save(Bid bid) {
        throw new UnsupportedOperationException("Use saveForAuction(auctionId, bid) instead.");
    }

    @Override
    public Optional<Bid> findById(Integer id) {
        String sql = """
                SELECT id, bidder_id, amount, timestamp
                FROM bids
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBid(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find bid by id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Bid> findAll() {
        String sql = """
                SELECT id, bidder_id, amount, timestamp
                FROM bids
                ORDER BY amount DESC
                """;

        List<Bid> bids = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                bids.add(mapResultSetToBid(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find all bids.", e);
        }

        return bids;
    }

    public List<Bid> findByAuctionId(Integer auctionId) {
        String sql = """
                SELECT id, bidder_id, amount, timestamp
                FROM bids
                WHERE auction_id = ?
                ORDER BY amount DESC
                """;

        List<Bid> bids = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, auctionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bids.add(mapResultSetToBid(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find bids by auction id.", e);
        }

        return bids;
    }

    public Optional<Bid> findHighestBidByAuctionId(Integer auctionId) {
        String sql = """
                SELECT id, bidder_id, amount, timestamp
                FROM bids
                WHERE auction_id = ?
                ORDER BY amount DESC
                LIMIT 1
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, auctionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBid(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find highest bid by auction id.", e);
        }

        return Optional.empty();
    }

    @Override
    public void update(Bid bid) {
        String sql = """
                UPDATE bids
                SET bidder_id = ?, amount = ?, timestamp = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bid.getBidder().getId());
            preparedStatement.setDouble(2, bid.getAmount());
            preparedStatement.setString(3, bid.getTimestamp().toString());
            preparedStatement.setInt(4, bid.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update bid.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM bids
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete bid.", e);
        }
    }

    private Bid mapResultSetToBid(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int bidderId = resultSet.getInt("bidder_id");
        double amount = resultSet.getDouble("amount");
        LocalDateTime timestamp = LocalDateTime.parse(resultSet.getString("timestamp"));

        User user = userRepository.findById(bidderId)
                .orElseThrow(() -> new SQLException("Bidder not found for id: " + bidderId));

        if (!(user instanceof Bidder bidder)) {
            throw new SQLException("User with id " + bidderId + " is not a bidder.");
        }

        return new Bid(id, bidder, amount, timestamp);
    }
}