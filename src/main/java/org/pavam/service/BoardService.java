package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.persistence.dao.BoardDAO;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

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
