package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.persistence.dao.BoardColumnDAO;
import org.pavam.persistence.dao.BoardDAO;
import org.pavam.persistence.entity.BoardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final long id) throws SQLException {
        var boardDao = new BoardDAO(connection);
        var boardColumnsDao = new BoardColumnDAO(connection);
        var optionalBoardEntity = boardDao.findById(id);
        if(optionalBoardEntity.isPresent()) {
            var boardEntity = optionalBoardEntity.get();
            boardEntity.setBoardColumns(boardColumnsDao.findByBoardId(boardEntity.getId()));
            return Optional.of(boardEntity);
        }
        return Optional.empty();
    }

}
