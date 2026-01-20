package org.pavam.service;

import lombok.AllArgsConstructor;
import org.pavam.dto.CardDetailsDTO;
import org.pavam.persistence.dao.CardDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final Long cardId) throws SQLException {
        var cardDao = new CardDAO(connection);
        return cardDao.findCardById(cardId);
    }

}
