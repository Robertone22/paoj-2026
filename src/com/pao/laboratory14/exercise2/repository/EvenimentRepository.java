package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {
    private final Connection connection;

    public EvenimentRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void initSchema() {
        String dropSql = "DROP TABLE IF EXISTS evenimente";

        String createSql = """
                CREATE TABLE IF NOT EXISTS evenimente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nume TEXT NOT NULL,
                    data TEXT NOT NULL,
                    capacitate INTEGER,
                    tip TEXT
                )
                """;

        try (PreparedStatement dropStatement = connection.prepareStatement(dropSql);
             PreparedStatement createStatement = connection.prepareStatement(createSql)) {

            dropStatement.executeUpdate();
            createStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize schema.", e);
        }
    }

    @Override
    public void save(Eveniment eveniment) {
        String sql = """
                INSERT INTO evenimente (nume, data, capacitate, tip)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, eveniment.getNume());
            preparedStatement.setString(2, eveniment.getData());
            preparedStatement.setInt(3, eveniment.getCapacitate());
            preparedStatement.setString(4, eveniment.getTip().name());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    eveniment.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not save event.", e);
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) {
        String sql = "SELECT * FROM evenimente WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEveniment(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find event by id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() {
        String sql = "SELECT * FROM evenimente ORDER BY id";
        List<Eveniment> evenimente = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                evenimente.add(mapResultSetToEveniment(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find events.", e);
        }

        return evenimente;
    }

    @Override
    public void update(Eveniment eveniment) {
        String sql = """
                UPDATE evenimente
                SET nume = ?, data = ?, capacitate = ?, tip = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, eveniment.getNume());
            preparedStatement.setString(2, eveniment.getData());
            preparedStatement.setInt(3, eveniment.getCapacitate());
            preparedStatement.setString(4, eveniment.getTip().name());
            preparedStatement.setInt(5, eveniment.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update event.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        deleteImpl(id);
    }

    public int deleteImpl(int id) {
        String sql = "DELETE FROM evenimente WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete event.", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM evenimente";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not count events.", e);
        }

        return 0;
    }

    private Eveniment mapResultSetToEveniment(ResultSet resultSet) throws SQLException {
        return new Eveniment(
                resultSet.getInt("id"),
                resultSet.getString("nume"),
                resultSet.getString("data"),
                resultSet.getInt("capacitate"),
                TipBilet.valueOf(resultSet.getString("tip"))
        );
    }
}