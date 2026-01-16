package org.pavam.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.pavam.persistence.entity.BoardColumnEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(BoardColumnEntity boardColumnEntity) throws SQLException {
        var sqlCommand = "INSERT INTO BOARD_COLUMNS " +
                "(description, `order`, kind, board_id) " +
                "VALUES (?, ?, ?, ?)";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            int i = 1;
            statement.setString(i++, boardColumnEntity.getDescription());
            statement.setInt(i++, boardColumnEntity.getOrder());
            statement.setString(i++, boardColumnEntity.getKind().name());
            statement.setLong(i, boardColumnEntity.getBoardEntity().getId());
            if(statement instanceof StatementImpl impl) {
                boardColumnEntity.setId(impl.getLastInsertID());
            }
            return boardColumnEntity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long boardId) throws SQLException {
        var sqlCommand = "SELECT * FROM BOARD_COLUMNS WHERE board_id = ?";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            return null;
        }
    }
}
