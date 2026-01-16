package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.persistence.dao.BoardColumnDAO;
import org.pavam.persistence.dao.BoardDAO;
import org.pavam.persistence.entity.BoardEntity;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity boardEntity) throws SQLException {
        var boardDAO = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        try {
            boardDAO.insert(boardEntity);
            var boardColumnList = boardEntity.getBoardColumns().stream().map(c ->
            {
                c.setBoardEntity(boardEntity);
                return c;
            }).toList();
            for(var column : boardColumnList) {
                boardColumnDAO.insert(column);
            }
            connection.commit();
            return boardEntity;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean delete(final long id) throws SQLException {
        var boardDAO = new BoardDAO(connection);
        try {
            if(!boardDAO.exists(id)) {
                return false;
            }
            boardDAO.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
