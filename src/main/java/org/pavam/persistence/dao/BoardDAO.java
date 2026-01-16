package org.pavam.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.pavam.persistence.entity.BoardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public BoardEntity insert(BoardEntity boardEntity) throws SQLException {
        var sqlCommand = "INSERT INTO BOARD (name) VALUES (?) ";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, boardEntity.getName());
            statement.executeUpdate();
            if(statement instanceof StatementImpl impl) {
                boardEntity.setId(impl.getLastInsertID());
            }
            return boardEntity;
        }
    }

    public final void delete(Long id) throws SQLException {
        var sqlCommand = "DELETE FROM BOARD WHERE id = ?";
        int countDeletedRows;
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, id);
            countDeletedRows = statement.executeUpdate();
            System.out.printf("Delete %d rows.", countDeletedRows);
        }
    }

    public final Optional<BoardEntity> findById(Long id) throws SQLException {
        var sqlCommand = "SELECT id, name FROM BOARD WHERE id = ?";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if(resultSet.next()) {
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public final boolean exists(Long id) throws SQLException {
        var sqlCommand = "SELECT 1 FROM BOARD WHERE id = ?";
        try(var statement = connection.prepareStatement(sqlCommand)) {
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }
    }

}
