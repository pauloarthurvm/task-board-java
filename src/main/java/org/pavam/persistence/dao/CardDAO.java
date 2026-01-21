package org.pavam.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.pavam.dto.CardDetailsDTO;
import org.pavam.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.pavam.persistence.converter.OffsetDateTimeConverter.convertToOffSetDateTime;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public CardEntity insert(final CardEntity cardEntity) throws SQLException {
        var sqlCommand = "INSERT INTO CARDS (title, description, board_column_id) VALUES (?, ?, ?);";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            var i = 1;
            statement.setString(i++, cardEntity.getTitle());
            statement.setString(i++, cardEntity.getDescription());
            statement.setLong(i, cardEntity.getBoardColumnEntity().getId());
            statement.executeUpdate();
            if (statement instanceof StatementImpl impl) {
                cardEntity.setId(impl.getLastInsertID());
            }
        }
        return cardEntity;
    }

    public Optional<CardDetailsDTO> findCardById(final Long cardId) throws SQLException {
        var sqlCommand = """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.description,
                       (SELECT COUNT(sub_b.id)
                               FROM BLOCKS sub_b
                              WHERE sub_b.card_id = c.id) blocks_amount
                  FROM CARDS c
                  LEFT JOIN BLOCKS b
                    ON c.id = b.card_id
                   AND b.unblocked_at IS NULL
                 INNER JOIN BOARD_COLUMNS bc
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
                        nonNull(resultSet.getString("b.block_reason")),
                        convertToOffSetDateTime(resultSet.getTimestamp("b.blocked_at")),
                        resultSet.getString("b.block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("c.board_column_id"),
                        resultSet.getString("bc.description")
                );
                return Optional.of(cardDetailsDTO);
            }
        }
        return Optional.empty();
    }
}
