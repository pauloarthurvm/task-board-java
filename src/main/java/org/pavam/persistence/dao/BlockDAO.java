package org.pavam.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static org.pavam.persistence.converter.OffsetDateTimeConverter.convertToTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;


    public void blockCard(final Long cardIdToBlock, final String blockingReason) throws SQLException {
        var sqlStatement = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
        try(var statement = connection.prepareStatement(sqlStatement)) {
            var i = 1;
            statement.setTimestamp(i++, convertToTimestamp(OffsetDateTime.now()));
            statement.setString(i++, blockingReason);
            statement.setLong(i, cardIdToBlock);
            statement.executeUpdate();
        }
    }
}
