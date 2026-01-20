package org.pavam.persistence.dao;

import lombok.AllArgsConstructor;
import org.pavam.dto.CardDetailsDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.pavam.persistence.converter.OffsetDateTimeConverter.convertToOffSetDateTime;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public Optional<CardDetailsDTO> findCardById(final Long cardId) throws SQLException {
        var sqlCommand = """
            SELECT
                c.id,
                c.title,
                c.description,
                c.board_column_id
                b.blocked_at,
                b.block_reason,
                bc.name,
                COUNT(SELECT sub_b.id
                    FROM BLOCKS sub_b
                    WHERE sub_b.card_id = c.id) blocks_amount
            FROM CARD c
            LEFT JOIN BLOCKS b
                ON c.id = b.card_id
                AND b.unblock_at IS NULL
            INNER BOARD_COLUMNS bc
                ON bc.id = c.board_column_id
            WHERE c.id = ?;
        """;
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, cardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if(resultSet.next()) {
                var cardDetailsDTO = new CardDetailsDTO(
                        resultSet.getLong("c.id"),
                        resultSet.getString("c.title"),
                        resultSet.getString("c.description"),
                        resultSet.getString("b.block_reason").isEmpty(),
                        convertToOffSetDateTime(resultSet.getTimestamp("b.blocked_at")),
                        resultSet.getString("b.block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("c.board_column_id"),
                        resultSet.getString("bc.name")
                );
                return Optional.of(cardDetailsDTO);
            }
        }
        return Optional.empty();
    }
}
