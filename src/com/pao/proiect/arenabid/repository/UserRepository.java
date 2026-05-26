package com.pao.proiect.arenabid.repository;

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

public class UserRepository implements Repository<User, Integer> {
    private final Connection connection;

    public UserRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users (id, name, email, role, won_auctions_count)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getRole());

            if (user instanceof Bidder bidder) {
                preparedStatement.setInt(5, bidder.getWonAuctionsCount());
            } else {
                preparedStatement.setInt(5, 0);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save user.", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = """
                SELECT id, name, email, role, won_auctions_count
                FROM users
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find user by id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = """
                SELECT id, name, email, role, won_auctions_count
                FROM users
                """;

        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find all users.", e);
        }

        return users;
    }

    @Override
    public void update(User user) {
        String sql = """
                UPDATE users
                SET name = ?, email = ?, role = ?, won_auctions_count = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getRole());

            if (user instanceof Bidder bidder) {
                preparedStatement.setInt(4, bidder.getWonAuctionsCount());
            } else {
                preparedStatement.setInt(4, 0);
            }

            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update user.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete user.", e);
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String role = resultSet.getString("role");
        int wonAuctionsCount = resultSet.getInt("won_auctions_count");

        if ("SELLER".equalsIgnoreCase(role)) {
            return new Seller(id, name, email);
        }

        if ("BIDDER".equalsIgnoreCase(role)) {
            Bidder bidder = new Bidder(id, name, email);
            bidder.setWonAuctionsCount(wonAuctionsCount);
            return bidder;
        }

        throw new SQLException("Unknown user role: " + role);
    }
}