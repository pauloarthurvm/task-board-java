package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.dto.BoardDetailsDTO;
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

    public Optional<BoardDetailsDTO> showBoardDetails(final long id) throws SQLException {
        var boardDao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        var boardEntityOptional = boardDao.findById(id);
        if(boardEntityOptional.isPresent()) {
            var boardEntity = boardEntityOptional.get();
            var boardColumnDtoList = boardColumnDao.findByBoardIdWithDetails(id);
            var boardDetailDto = new BoardDetailsDTO(boardEntity.getId(), boardEntity.getName(), boardColumnDtoList);
            return Optional.of(boardDetailDto);
        }
        return Optional.empty();
    }

}
