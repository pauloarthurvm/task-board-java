package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.persistence.dao.BoardColumnDAO;
import org.pavam.persistence.entity.BoardColumnEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final Long columnId) throws SQLException {
        var boardColumnDao = new BoardColumnDAO(connection);
        return boardColumnDao.findById(columnId);
    }

}
