package com.pao.proiect.arenabid.repository;

import com.pao.proiect.arenabid.model.AuctionItem;
import com.pao.proiect.arenabid.model.AuthenticityCertificate;
import com.pao.proiect.arenabid.model.ItemCondition;
import com.pao.proiect.arenabid.model.RarityLevel;
import com.pao.proiect.arenabid.model.SportType;
import com.pao.proiect.arenabid.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuctionItemRepository implements Repository<AuctionItem, Integer> {
    private final Connection connection;

    public AuctionItemRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(AuctionItem item) {
        saveCertificateIfPresent(item.getCertificate());

        String sql = """
                INSERT INTO auction_items (
                    id, name, sport_type, athlete_name, rarity_level,
                    item_condition, starting_price, premium, certificate_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setString(3, item.getSportType().name());
            preparedStatement.setString(4, item.getAthleteName());
            preparedStatement.setString(5, item.getRarityLevel().name());
            preparedStatement.setString(6, item.getItemCondition().name());
            preparedStatement.setDouble(7, item.getStartingPrice());
            preparedStatement.setInt(8, item.isPremium() ? 1 : 0);

            if (item.getCertificate() != null) {
                preparedStatement.setString(9, item.getCertificate().getCertificateId());
            } else {
                preparedStatement.setString(9, null);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save auction item.", e);
        }
    }

    @Override
    public Optional<AuctionItem> findById(Integer id) {
        String sql = """
                SELECT ai.id, ai.name, ai.sport_type, ai.athlete_name, ai.rarity_level,
                       ai.item_condition, ai.starting_price, ai.premium,
                       ac.certificate_id, ac.issued_by, ac.issue_date, ac.verified
                FROM auction_items ai
                LEFT JOIN authenticity_certificates ac ON ai.certificate_id = ac.certificate_id
                WHERE ai.id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToAuctionItem(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find auction item by id.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<AuctionItem> findAll() {
        String sql = """
                SELECT ai.id, ai.name, ai.sport_type, ai.athlete_name, ai.rarity_level,
                       ai.item_condition, ai.starting_price, ai.premium,
                       ac.certificate_id, ac.issued_by, ac.issue_date, ac.verified
                FROM auction_items ai
                LEFT JOIN authenticity_certificates ac ON ai.certificate_id = ac.certificate_id
                """;

        List<AuctionItem> items = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                items.add(mapResultSetToAuctionItem(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not find all auction items.", e);
        }

        return items;
    }

    @Override
    public void update(AuctionItem item) {
        saveCertificateIfPresent(item.getCertificate());

        String sql = """
                UPDATE auction_items
                SET name = ?, sport_type = ?, athlete_name = ?, rarity_level = ?,
                    item_condition = ?, starting_price = ?, premium = ?, certificate_id = ?
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getSportType().name());
            preparedStatement.setString(3, item.getAthleteName());
            preparedStatement.setString(4, item.getRarityLevel().name());
            preparedStatement.setString(5, item.getItemCondition().name());
            preparedStatement.setDouble(6, item.getStartingPrice());
            preparedStatement.setInt(7, item.isPremium() ? 1 : 0);

            if (item.getCertificate() != null) {
                preparedStatement.setString(8, item.getCertificate().getCertificateId());
            } else {
                preparedStatement.setString(8, null);
            }

            preparedStatement.setInt(9, item.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update auction item.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                DELETE FROM auction_items
                WHERE id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete auction item.", e);
        }
    }

    private void saveCertificateIfPresent(AuthenticityCertificate certificate) {
        if (certificate == null) {
            return;
        }

        String sql = """
                INSERT OR IGNORE INTO authenticity_certificates (
                    certificate_id, issued_by, issue_date, verified
                )
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, certificate.getCertificateId());
            preparedStatement.setString(2, certificate.getIssuedBy());
            preparedStatement.setString(3, certificate.getIssueDate().toString());
            preparedStatement.setInt(4, certificate.isVerified() ? 1 : 0);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not save authenticity certificate.", e);
        }
    }

    private AuctionItem mapResultSetToAuctionItem(ResultSet resultSet) throws SQLException {
        AuthenticityCertificate certificate = null;

        String certificateId = resultSet.getString("certificate_id");
        if (certificateId != null) {
            certificate = new AuthenticityCertificate(
                    certificateId,
                    resultSet.getString("issued_by"),
                    java.time.LocalDate.parse(resultSet.getString("issue_date")),
                    resultSet.getInt("verified") == 1
            );
        }

        return new AuctionItem(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                SportType.valueOf(resultSet.getString("sport_type")),
                resultSet.getString("athlete_name"),
                RarityLevel.valueOf(resultSet.getString("rarity_level")),
                ItemCondition.valueOf(resultSet.getString("item_condition")),
                resultSet.getDouble("starting_price"),
                resultSet.getInt("premium") == 1,
                certificate
        );
    }
}