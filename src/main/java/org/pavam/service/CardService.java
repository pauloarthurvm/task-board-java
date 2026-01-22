package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.dto.BoardColumnInfoDTO;
import org.pavam.persistence.dao.BlockDAO;
import org.pavam.persistence.dao.CardDAO;
import org.pavam.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.pavam.persistence.entity.BoardColumnKindEnum.CANCEL;
import static org.pavam.persistence.entity.BoardColumnKindEnum.FINAL;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public final CardEntity insert(CardEntity cardEntity) throws SQLException {
        try {
            var cardDao = new CardDAO(connection);
            cardDao.insert(cardEntity);
            connection.commit();
            return cardEntity;
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

    public void moveToNextColumn(final Long cardIdToMove, final List<BoardColumnInfoDTO> boardColumnInfoDtoList) throws SQLException {
        try {
            var cardDao = new CardDAO(connection);
            var optionalCardDetailsDto = cardDao.findCardById(cardIdToMove);
            var cardDetailsDto = optionalCardDetailsDto.orElseThrow(() ->
                    new RuntimeException("Not found - Card ID %d".formatted(cardIdToMove)));
            if(cardDetailsDto.blocked()) {
                throw new RuntimeException("Card is blocked - Card ID %d".formatted(cardIdToMove));
            }
            var currentColumnInfoDto = boardColumnInfoDtoList.stream()
                    .filter(bcinfoDto -> bcinfoDto.id().equals(cardDetailsDto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Informed card belongs to another board"));
            if(currentColumnInfoDto.kind().equals(FINAL)) {
                throw new RuntimeException("Card is already in Final Column");
            }
            var nextColumnInfoDto = boardColumnInfoDtoList.stream()
                    .filter(bc -> bc.order() == currentColumnInfoDto.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Card is Canceled - Card column: %s".formatted(cardDetailsDto.columnName())));
            cardDao.moveToColumn(cardIdToMove, nextColumnInfoDto.id());
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void cancelCard(final long cardIdToCancel,
                           final Long cancelColumnId,
                           final List<BoardColumnInfoDTO> boardColumnInfoDtoList) throws SQLException {
        try {
            var cardDao = new CardDAO(connection);
            var optionalCardDetailsDto = cardDao.findCardById(cardIdToCancel);
            var cardDetailsDto = optionalCardDetailsDto.orElseThrow(() ->
                    new RuntimeException("Not found - Card ID %d".formatted(cardIdToCancel)));
            if(cardDetailsDto.blocked()) {
                throw new RuntimeException("Card is blocked - Card ID %d".formatted(cardIdToCancel));
            }
            var currentColumnInfoDto = boardColumnInfoDtoList.stream()
                    .filter(bcinfoDto -> bcinfoDto.id().equals(cardDetailsDto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Informed card belongs to another board"));
            if(currentColumnInfoDto.kind().equals(FINAL)) {
                throw new RuntimeException("Card is already in Final Column");
            }
            boardColumnInfoDtoList.stream()
                    .filter(bc -> bc.order() == currentColumnInfoDto.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Card is Canceled - Card column: %s".formatted(cardDetailsDto.columnName())));
            cardDao.moveToColumn(cardIdToCancel, cancelColumnId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void block(final Long cardIdToBlock,
                      final String blockingReason,
                      final List<BoardColumnInfoDTO> boardColumnInfoDtoList) throws SQLException {
        try {
            var cardDao = new CardDAO(connection);
            var optionalCardDetailsDto = cardDao.findCardById(cardIdToBlock);
            var cardDetailsDto = optionalCardDetailsDto.orElseThrow(() ->
                    new RuntimeException("Not found - Card ID %d".formatted(cardIdToBlock)));
            if(cardDetailsDto.blocked()) {
                throw new RuntimeException("Card is already blocked - Card ID %d".formatted(cardIdToBlock));
            }
            var currentColumnInfoDto = boardColumnInfoDtoList.stream()
                    .filter(bcinfoDto -> bcinfoDto.id().equals(cardDetailsDto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Informed card belongs to another board"));
            if(currentColumnInfoDto.kind().equals(FINAL) || currentColumnInfoDto.kind().equals(CANCEL)) {
                throw new IllegalStateException(
                        "Card is in %s Column.\nCant be blocked.\n".formatted(currentColumnInfoDto.kind()));
            }
            new BlockDAO(connection).blockCard(cardIdToBlock, blockingReason);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }
}
