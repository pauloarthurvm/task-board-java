package org.pavam.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.pavam.persistence.entity.BoardColumnEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.pavam.persistence.entity.BoardColumnKindEnum.getKindEnumByName;

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
        List<BoardColumnEntity> boardColumnEntityList = new ArrayList<>();
        var sqlCommand = "SELECT * FROM BOARD_COLUMNS WHERE board_id = ? ORDER BY `order`";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while(resultSet.next()) {
                var boardColumnEntity = new BoardColumnEntity();
                boardColumnEntity.setId(resultSet.getLong("id"));
                boardColumnEntity.setDescription(resultSet.getString("description"));
                boardColumnEntity.setOrder(resultSet.getInt("order"));
                boardColumnEntity.setKind(getKindEnumByName(resultSet.getString("kind")));
                boardColumnEntityList.add(boardColumnEntity);
            }
            return boardColumnEntityList;
        }
    }
}
